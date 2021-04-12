package com.probending.probending.config;

import com.probending.probending.ProBending;
import com.probending.probending.core.annotations.Language;
import com.probending.probending.util.UtilMethods;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class LanguageConfig extends AbstractConfig {

    private static final HashMap<Class<?>, List<Field>> ANNOTATIONS_BY_CLASS = new HashMap<>();

    public LanguageConfig(String path, ProBending plugin) {
        super(path, plugin);
        registerAnnotations();
    }

    @Override
    public void reload() {
        super.reload();
        reloadAnnotations();
    }

    private void registerAnnotations() {
        List<Class<?>> classes = UtilMethods.findClasses();

        if (classes == null) {
            ProBending.plugin.log(Level.WARNING, "Failed getting all classes!");
            return;
        }

        Language annotation;

        for (Class<?> clazz : classes) {
            ANNOTATIONS_BY_CLASS.put(clazz, new ArrayList<>());
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Language.class)) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        annotation = field.getAnnotation(Language.class);
                        ANNOTATIONS_BY_CLASS.get(clazz).add(field);
                        try {
                            addDefault(annotation.value(), field.get(null));
                            field.set(null, get(annotation.value()));
                        } catch (Exception e) {
                            ProBending.plugin.log(Level.WARNING, "Error loading language annotations in " + field.getName() + ", in class " + clazz.getName());
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
        save();
    }

    private void reloadAnnotations() {
        for (Class<?> clazz : ANNOTATIONS_BY_CLASS.keySet()) {
            for (Field field : ANNOTATIONS_BY_CLASS.get(clazz)) {
                try {
                    field.set(null, get(field.getAnnotation(Language.class).value()));
                } catch (Exception e) {
                    ProBending.plugin.log(Level.WARNING, "Error reloading language annotations in " + field.getName() + ", in class " + clazz.getName());
                    e.printStackTrace();
                }
            }
        }
    }
}
