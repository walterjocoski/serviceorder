package com.rfpiscinas.serviceorder.data.repository;

import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao;
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
public final class ServiceOrderRepository_Factory implements Factory<ServiceOrderRepository> {
  private final Provider<ServiceOrderDao> serviceOrderDaoProvider;

  public ServiceOrderRepository_Factory(Provider<ServiceOrderDao> serviceOrderDaoProvider) {
    this.serviceOrderDaoProvider = serviceOrderDaoProvider;
  }

  @Override
  public ServiceOrderRepository get() {
    return newInstance(serviceOrderDaoProvider.get());
  }

  public static ServiceOrderRepository_Factory create(
      Provider<ServiceOrderDao> serviceOrderDaoProvider) {
    return new ServiceOrderRepository_Factory(serviceOrderDaoProvider);
  }

  public static ServiceOrderRepository newInstance(ServiceOrderDao serviceOrderDao) {
    return new ServiceOrderRepository(serviceOrderDao);
  }
}
