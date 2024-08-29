package dev.jean.apps.healthserver;

import org.springframework.transaction.annotation.Transactional;

import dev.jean.apps.ApplicationTestCase;

@Transactional("healthserver-transaction_manager")
public class HealthServerApplicationTestCase extends ApplicationTestCase {
}
