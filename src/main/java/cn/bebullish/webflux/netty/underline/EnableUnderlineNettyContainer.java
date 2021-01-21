package cn.bebullish.webflux.netty.underline;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * enable netty container with underline support for domain names
 *
 * @author Marlon
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UnderlineReactiveWebServerFactoryConfiguration.class)
public @interface EnableUnderlineNettyContainer {
}
