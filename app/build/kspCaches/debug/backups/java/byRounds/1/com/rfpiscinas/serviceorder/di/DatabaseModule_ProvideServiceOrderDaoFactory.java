package com.rfpiscinas.serviceorder.di;

import com.rfpiscinas.serviceorder.data.local.AppDatabase;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideServiceOrderDaoFactory implements Factory<ServiceOrderDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideServiceOrderDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ServiceOrderDao get() {
    return provideServiceOrderDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideServiceOrderDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideServiceOrderDaoFactory(dbProvider);
  }

  public static ServiceOrderDao provideServiceOrderDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideServiceOrderDao(db));
  }
}
