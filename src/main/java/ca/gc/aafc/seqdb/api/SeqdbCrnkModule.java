package ca.gc.aafc.seqdb.api;

import javax.inject.Named;

import org.springframework.context.ApplicationContext;

import io.crnk.core.engine.error.ExceptionMapper;
import io.crnk.core.module.Module;
import lombok.RequiredArgsConstructor;

/**
 * Module to register Seqdb features with Crnk.
 */
@Named
@RequiredArgsConstructor
public class SeqdbCrnkModule implements Module {
  
  private final ApplicationContext applicationContext;
  
  @Override
  public String getModuleName() {
    return "seqdb";
  }

  @Override
  public void setupModule(ModuleContext context) {
    applicationContext.getBeansOfType(ExceptionMapper.class)
        .values()
        .forEach(context::addExceptionMapper);
  }
  
}
