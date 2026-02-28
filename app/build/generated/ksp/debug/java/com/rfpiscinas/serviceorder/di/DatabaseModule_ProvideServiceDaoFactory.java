package com.rfpiscinas.serviceorder.di;

import com.rfpiscinas.serviceorder.data.local.AppDatabase;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao;
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
public final class DatabaseModule_ProvideServiceDaoFactory implements Factory<ServiceDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideServiceDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ServiceDao get() {
    return provideServiceDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideServiceDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideServiceDaoFactory(dbProvider);
  }

  public static ServiceDao provideServiceDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideServiceDao(db));
  }
}
