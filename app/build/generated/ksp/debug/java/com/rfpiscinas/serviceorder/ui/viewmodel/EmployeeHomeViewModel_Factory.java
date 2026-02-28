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
public final class EmployeeHomeViewModel_Factory implements Factory<EmployeeHomeViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  public EmployeeHomeViewModel_Factory(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
  }

  @Override
  public EmployeeHomeViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get());
  }

  public static EmployeeHomeViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider) {
    return new EmployeeHomeViewModel_Factory(serviceOrderRepositoryProvider);
  }

  public static EmployeeHomeViewModel newInstance(ServiceOrderRepository serviceOrderRepository) {
    return new EmployeeHomeViewModel(serviceOrderRepository);
  }
}
