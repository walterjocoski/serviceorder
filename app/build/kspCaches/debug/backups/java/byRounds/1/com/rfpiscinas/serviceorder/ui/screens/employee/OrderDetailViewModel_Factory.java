package com.rfpiscinas.serviceorder.ui.screens.employee;

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
public final class OrderDetailViewModel_Factory implements Factory<OrderDetailViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  public OrderDetailViewModel_Factory(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
  }

  @Override
  public OrderDetailViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get());
  }

  public static OrderDetailViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    return new OrderDetailViewModel_Factory(serviceOrderRepositoryProvider);
  }

  public static OrderDetailViewModel newInstance(ServiceOrderRepository serviceOrderRepository) {
    return new OrderDetailViewModel(serviceOrderRepository);
  }
}
