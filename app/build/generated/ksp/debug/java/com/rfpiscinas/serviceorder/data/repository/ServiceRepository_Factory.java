package com.rfpiscinas.serviceorder.data.repository;

import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ServiceRepository_Factory implements Factory<ServiceRepository> {
  private final Provider<ServiceDao> serviceDaoProvider;

  public ServiceRepository_Factory(Provider<ServiceDao> serviceDaoProvider) {
    this.serviceDaoProvider = serviceDaoProvider;
  }

  @Override
  public ServiceRepository get() {
    return newInstance(serviceDaoProvider.get());
  }

  public static ServiceRepository_Factory create(Provider<ServiceDao> serviceDaoProvider) {
    return new ServiceRepository_Factory(serviceDaoProvider);
  }

  public static ServiceRepository newInstance(ServiceDao serviceDao) {
    return new ServiceRepository(serviceDao);
  }
}
