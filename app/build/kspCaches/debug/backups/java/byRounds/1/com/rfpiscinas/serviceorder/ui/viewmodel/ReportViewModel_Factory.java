package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.ClientRepository;
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
public final class ReportViewModel_Factory implements Factory<ReportViewModel> {
  private final Provider<ServiceOrderRepository> serviceOrderRepositoryProvider;

  private final Provider<ClientRepository> clientRepositoryProvider;

  public ReportViewModel_Factory(Provider<ServiceOrderRepository> serviceOrderRepositoryProvider,
      Provider<ClientRepository> clientRepositoryProvider) {
    this.serviceOrderRepositoryProvider = serviceOrderRepositoryProvider;
    this.clientRepositoryProvider = clientRepositoryProvider;
  }

  @Override
  public ReportViewModel get() {
    return newInstance(serviceOrderRepositoryProvider.get(), clientRepositoryProvider.get());
  }

  public static ReportViewModel_Factory create(
      Provider<ServiceOrderRepository> serviceOrderRepositoryProvider,
      Provider<ClientRepository> clientRepositoryProvider) {
    return new ReportViewModel_Factory(serviceOrderRepositoryProvider, clientRepositoryProvider);
  }

  public static ReportViewModel newInstance(ServiceOrderRepository serviceOrderRepository,
      ClientRepository clientRepository) {
    return new ReportViewModel(serviceOrderRepository, clientRepository);
  }
}
