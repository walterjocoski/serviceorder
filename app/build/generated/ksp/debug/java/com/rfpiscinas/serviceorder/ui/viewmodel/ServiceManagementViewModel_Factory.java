package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ServiceRepository;
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
public final class ServiceManagementViewModel_Factory implements Factory<ServiceManagementViewModel> {
  private final Provider<ServiceRepository> serviceRepositoryProvider;

  public ServiceManagementViewModel_Factory(Provider<ServiceRepository> serviceRepositoryProvider) {
    this.serviceRepositoryProvider = serviceRepositoryProvider;
  }

  @Override
  public ServiceManagementViewModel get() {
    return newInstance(serviceRepositoryProvider.get());
  }

  public static ServiceManagementViewModel_Factory create(
      Provider<ServiceRepository> serviceRepositoryProvider) {
    return new ServiceManagementViewModel_Factory(serviceRepositoryProvider);
  }

  public static ServiceManagementViewModel newInstance(ServiceRepository serviceRepository) {
    return new ServiceManagementViewModel(serviceRepository);
  }
}
