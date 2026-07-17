package com.calculator.listener;

import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

/**
 * Listener que imprime en consola el @DisplayName de cada test
 * al finalizar su ejecución.
 *
 * Se registra automáticamente via ServiceLoader (ver
 * META-INF/services/org.junit.platform.launcher.TestExecutionListener).
 *
 * Formato de salida:
 *   [TEST]  ✔  CalculatorService – Unit Tests > add() > suma dos positivos
 *   [TEST]  ✔  CalculatorService – Unit Tests > divide() > BRANCH: divisor = 0 → lanza DivisionByZeroException
 *   [TEST]  ✘  CalculatorService – Unit Tests > divide() > algún test fallido
 */
public class DisplayNameListener implements TestExecutionListener {

    private TestPlan testPlan;

    @Override
    public void testPlanExecutionStarted(TestPlan plan) {
        this.testPlan = plan;
        System.out.println();
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("  EJECUCIÓN DE PRUEBAS UNITARIAS");
        System.out.println("══════════════════════════════════════════════════════════════");
    }

    @Override
    public void executionFinished(TestIdentifier id, TestExecutionResult result) {
        // Solo nos interesan los tests hoja (métodos reales, no clases ni @Nested)
        if (!id.isTest()) {
            return;
        }

        String status = switch (result.getStatus()) {
            case SUCCESSFUL -> "✔";
            case FAILED     -> "✘";
            case ABORTED    -> "⚠";
        };

        String displayPath = buildDisplayPath(id);

        System.out.printf("  [TEST] %s  %s%n", status, displayPath);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan plan) {
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println();
    }

    // -------------------------------------------------------------------------

    /**
     * Construye la ruta legible del test usando los @DisplayName de todos
     * los ancestros (clase raíz → @Nested → método).
     * Ejemplo: "CalculatorService – Unit Tests > add() > suma dos positivos"
     */
    private String buildDisplayPath(TestIdentifier id) {
        StringBuilder path = new StringBuilder();
        appendAncestors(id, path);
        return path.toString();
    }

    private void appendAncestors(TestIdentifier id, StringBuilder path) {
        testPlan.getParent(id).ifPresent(parent -> {
            // Salta el nodo raíz del engine (no tiene nombre útil)
            if (testPlan.getParent(parent).isPresent()) {
                appendAncestors(parent, path);
                path.append(" > ");
            }
        });
        path.append(id.getDisplayName());
    }
}
