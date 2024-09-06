package dev.jean.shared.infrastructure;

/**
 * This class is a base class for infrastructure test cases.
 */
public abstract class InfrastructureTestCase {
    private final Integer MAX_ATTEMPTS = 3;
    private final Integer MILLIS_TO_WAIT_BETWEEN_RETRIES = 1000;

    /**
     * Run an assertion until it passes or the maximum number of attempts is
     * reached.
     * 
     * @param assertion The assertion to run.
     * @throws Exception
     */
    protected void eventually(Runnable assertion) throws Exception {
        int attempts = 0;
        boolean allOk = false;

        while (attempts < MAX_ATTEMPTS && !allOk) {
            try {
                assertion.run();
                allOk = true;
            } catch (Throwable e) {
                attempts++;

                if (attempts > MAX_ATTEMPTS) {
                    throw new Exception(
                            String.format("Could not assert after some retries. Last error: %s", e.getMessage()));
                }

                Thread.sleep(MILLIS_TO_WAIT_BETWEEN_RETRIES);
            }
        }
    }
}
