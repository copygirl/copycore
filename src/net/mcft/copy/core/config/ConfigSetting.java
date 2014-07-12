package net.mcft.copy.core.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSetting {
	
	String getConfigElementClass() default "";
	
	String getConfigEntryClass() default "";
	
	boolean requiresWorldRestart() default false;
	
	boolean requiresMinecraftRestart() default false;
	
}
