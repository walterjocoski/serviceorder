package com.rfpiscinas.serviceorder.ui.viewmodel;

import com.rfpiscinas.serviceorder.data.repository.UserRepository;
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
public final class EmployeeManagementViewModel_Factory implements Factory<EmployeeManagementViewModel> {
  private final Provider<UserRepository> userRepositoryProvider;

  public EmployeeManagementViewModel_Factory(Provider<UserRepository> userRepositoryProvider) {
    this.userRepositoryProvider = userRepositoryProvider;
  }

  @Override
  public EmployeeManagementViewModel get() {
    return newInstance(userRepositoryProvider.get());
  }

  public static EmployeeManagementViewModel_Factory create(
      Provider<UserRepository> userRepositoryProvider) {
    return new EmployeeManagementViewModel_Factory(userRepositoryProvider);
  }

  public static EmployeeManagementViewModel newInstance(UserRepository userRepository) {
    return new EmployeeManagementViewModel(userRepository);
  }
}
