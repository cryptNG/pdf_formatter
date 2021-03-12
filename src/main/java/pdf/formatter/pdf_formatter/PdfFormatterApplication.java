package pdf.formatter.pdf_formatter;

import java.net.URI;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import io.github.classgraph.ClassGraph;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import pdf.formatter.pdf_formatter.application.Initializer;
import pdf.formatter.pdf_formatter.controllers.*;

@SpringBootApplication

@ComponentScan(basePackageClasses = OrderController.class) // it only detects components automatically in the root

@ComponentScan(basePackageClasses = Initializer.class) // it only detects components automatically in the root folder
@OpenAPIDefinition(info = @Info(title = "PDF Formatter API", version = "1.0", description = "Documentation PDF Formatter API v1.0"))
public class PdfFormatterApplication {

	public static void main(String[] args) {
		printEnvironment(args);
		SpringApplication.run(PdfFormatterApplication.class, args);
	}

	private static void printEnvironment(String[] args) {

		System.out.println("######################### ENVIRONMENT CONFIG #########################");
		String logBackPath = System.getenv("LOGBACK_CONFIG_FILE");
		if (StringUtils.isAllBlank(logBackPath)) {
			System.out.println("ENVIRONMENT VARIABLE [LOGBACK_CONFIG_FILE] NOT SET");
		} else {
			System.out.println("ENVIRONMENT VARIABLE [LOGBACK_CONFIG_FILE] = " + logBackPath);
		}
		String httpsEnabled = System.getenv("HTTPS_ENABLED");
		if (StringUtils.isAllBlank(httpsEnabled)) {
			System.out.println("ENVIRONMENT VARIABLE [HTTPS_ENABLED] NOT SET (" + Boolean.valueOf(httpsEnabled) + ")");
		} else {
			System.out.println("ENVIRONMENT VARIABLE [HTTPS_ENABLED] = " + httpsEnabled);
		}
		System.out.println("-------");
		List<URI> classpath = new ClassGraph().getClasspathURIs();
		System.out.println("Printing classpath entries:");
		int all = 0;
		int valid = 0;
		for (URI url : classpath) {
			all = all + 1;
			if (url.getPath() != null && !url.getPath().equals("null")) {
				valid = valid + 1;
				System.out.println(url.getPath());
			}
		}
		System.out.println("Found " + all + " classpath entries, only " + valid + " of them were valid.");
		System.out.println("-------");

		System.out.println("######################### END         CONFIG #########################");
	}

	static String getMainClassName() {
		StackTraceElement trace[] = Thread.currentThread().getStackTrace();
		if (trace.length > 0) {
			return trace[trace.length - 1].getClassName();
		}
		return "Unknown";
	}

}
