package architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import io.swagger.v3.oas.annotations.Operation;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class LayeredArchitectureTest {

    @Test
    void layersShouldBeRespected() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.unq.crypto_exchange");

        Architectures.LayeredArchitecture architecture = Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("com.unq.crypto_exchange.api.controller..")
                .layer("Service").definedBy("com.unq.crypto_exchange.service..")
                .layer("Repository").definedBy("com.unq.crypto_exchange.repository..")
                .layer("Security").definedBy("com.unq.crypto_exchange.security..")

                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service", "Security")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service", "Security");

        architecture.check(importedClasses);
    }

    @Test
    void controllersMethodShouldReturnResponseEntity() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.unq.crypto_exchange.api.controller");

        ArchRuleDefinition.methods().that().areDeclaredInClassesThat().resideInAPackage("com.unq.crypto_exchange.api.controller..")
                .and().areAnnotatedWith(Operation.class).should().haveRawReturnType(ResponseEntity.class)
                .check(importedClasses);

    }
}

