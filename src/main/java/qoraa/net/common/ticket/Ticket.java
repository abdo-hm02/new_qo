package qoraa.net.common.ticket;

import lombok.Getter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Getter
public class Ticket {

    @Parameter(value = "ticket")
    private Long id;

    @Parameter(value = "tickets", delimiter = ",")
    private List<Long> ids;

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Parameter {

	/**
	 * Optionally defined delimiter used to split param value into an array
	 *
	 * @return string used to further split param value
	 */
	String delimiter() default "";

	/**
	 * In case we there will be need for collection of ids
	 *
	 * @return name of the web request parameter to read values for
	 */
	String value() default "";

    }

}
