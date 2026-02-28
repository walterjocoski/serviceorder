package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ServiceOrderRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AllOrdersViewModel_Factory implements Factory<AllOrdersViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  public AllOrdersViewModel_Factory(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
  }

  @Override
  public AllOrdersViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get());
  }

  public static AllOrdersViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    return new AllOrdersViewModel_Factory(serviceOrderRepositoryProvider);
  }

  public static AllOrdersViewModel newInstance(ServiceOrderRepository serviceOrderRepository) {
    return new AllOrdersViewModel(serviceOrderRepository);
  }
}
