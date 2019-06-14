package ca.gc.aafc.seqdb.api.security;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;

import ca.gc.aafc.seqdb.api.BaseIntegrationTest;
import ca.gc.aafc.seqdb.api.SeqdbApiLauncher;
import ca.gc.aafc.seqdb.api.security.SecurityRepositories.AccountRepository;
import ca.gc.aafc.seqdb.entities.Account;
import ca.gc.aafc.seqdb.entities.AccountsGroup;
import ca.gc.aafc.seqdb.entities.Group;

@RunWith(Enclosed.class)
public class ImportSampleAccountsIT {

  @SpringBootTest(classes = ImportSampleAccountsEnabledIT.TestConfig.class)
  public static class ImportSampleAccountsEnabledIT extends BaseIntegrationTest {

    /**
     * This is a custom configuration to load in our stubbed ImportSampleAccount.
     * All of the other defaults are taken from the SeqdbApiLauncher class.
     */
    @TestConfiguration
    @Import(SeqdbApiLauncher.class)
    public static class TestConfig {

      @Inject
      private ApplicationContext applicationContext;
      
      /**
       * Instead of running it with the property we just apply this bean with mocked
       * methods to avoid the hibernate queries from being performed.
       */
      @Bean
      @Primary
      public ImportSampleAccounts mockedBean() {
        // Retrieve the bean factory from the application context.
        AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) factory;
        
        // Create the bean.
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ImportSampleAccountsStub.class);
        beanDefinition.setAutowireCandidate(false);
        
        // Register the bean
        registry.registerBeanDefinition("importSampleAccountsStub", beanDefinition);

        // Now that it's registered, retrieve the bean that was created.
        return applicationContext.getBean(ImportSampleAccountsStub.class);
      }
    }
    
    @Inject
    private AccountRepository accountRepository;
    
    @Inject
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void startApp_whenImportSampleAccountsTrue_sampleAccountsAvailable() {
      // Check Admin account data
      Account adminAccount = accountRepository.findByAccountNameIgnoreCase("admin");
      assertTrue(passwordEncoder.matches("Admin", adminAccount.getAccountPw()));
      assertEquals("Admin", adminAccount.getAccountType());
      assertEquals("Active", adminAccount.getAccountStatus());
      
      AccountsGroup adminPermission = adminAccount.getAccountsGroups().iterator().next();
      assertEquals("1111", adminPermission.getRights());
      assertEquals(Boolean.TRUE, adminPermission.getAdmin());
      
      Group adminGroup = adminPermission.getGroup();
      assertEquals("Admin Group", adminGroup.getGroupName());
      
      // Check User account data
      Account userAccount = accountRepository.findByAccountNameIgnoreCase("user");
      assertTrue(passwordEncoder.matches("User", userAccount.getAccountPw()));
      assertEquals("User", userAccount.getAccountType());
      assertEquals("Active", userAccount.getAccountStatus());
      
      AccountsGroup userPermission = userAccount.getAccountsGroups().iterator().next();
      assertEquals("1111", userPermission.getRights());
      assertEquals(Boolean.TRUE, userPermission.getAdmin());
      
      Group userGroup = userPermission.getGroup();
      assertEquals("User Group", userGroup.getGroupName());
    }
    
  }
  
  public static class ImportSampleAccountsDisabledIT extends BaseIntegrationTest {
    
    @Inject
    private AccountRepository accountRepository;
    
    @Test
    @Ignore("unstable, depends on the order. See #16375")
    public void startApp_whenImportSampleAccountsNotSet_sampleAccountsNotAvailable() {
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("Admin"));
      assertNull(this.accountRepository.findByAccountNameIgnoreCase("User"));
    }
    
  }
  
}
