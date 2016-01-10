package com.javabatchmanager.watchers;
import java.lang.annotation.*;

import org.springframework.stereotype.Component;

@Component
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Monitor {

}
