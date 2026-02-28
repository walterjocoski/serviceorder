package com.rfpiscinas.serviceorder.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.rfpiscinas.serviceorder.data.local.dao.ClientDao;
import com.rfpiscinas.serviceorder.data.local.dao.ClientDao_Impl;
import com.rfpiscinas.serviceorder.data.local.dao.ProductDao;
import com.rfpiscinas.serviceorder.data.local.dao.ProductDao_Impl;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceDao_Impl;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao;
import com.rfpiscinas.serviceorder.data.local.dao.ServiceOrderDao_Impl;
import com.rfpiscinas.serviceorder.data.local.dao.UserDao;
import com.rfpiscinas.serviceorder.data.local.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ClientDao _clientDao;

  private volatile ServiceDao _serviceDao;

  private volatile ProductDao _productDao;

  private volatile UserDao _userDao;

  private volatile ServiceOrderDao _serviceOrderDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `clients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `cpfCnpj` TEXT NOT NULL, `address` TEXT NOT NULL, `phone` TEXT NOT NULL, `email` TEXT NOT NULL, `active` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `services` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `type` TEXT NOT NULL, `usesProducts` INTEGER NOT NULL, `active` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `unitMeasure` TEXT NOT NULL, `active` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `phone` TEXT NOT NULL, `address` TEXT NOT NULL, `role` TEXT NOT NULL, `active` INTEGER NOT NULL, `startDate` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `service_orders` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `clientId` INTEGER NOT NULL, `clientName` TEXT NOT NULL, `clientAddress` TEXT NOT NULL, `employeeId` INTEGER NOT NULL, `employeeName` TEXT NOT NULL, `status` TEXT NOT NULL, `startDateTime` TEXT NOT NULL, `endDateTime` TEXT, `synced` INTEGER NOT NULL, FOREIGN KEY(`clientId`) REFERENCES `clients`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`employeeId`) REFERENCES `users`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_orders_clientId` ON `service_orders` (`clientId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_orders_employeeId` ON `service_orders` (`employeeId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `service_order_items` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serviceOrderId` INTEGER NOT NULL, `serviceId` INTEGER NOT NULL, `serviceName` TEXT NOT NULL, `serviceType` TEXT NOT NULL, `serviceUsesProducts` INTEGER NOT NULL, `observations` TEXT NOT NULL, FOREIGN KEY(`serviceOrderId`) REFERENCES `service_orders`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`serviceId`) REFERENCES `services`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_order_items_serviceOrderId` ON `service_order_items` (`serviceOrderId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_order_items_serviceId` ON `service_order_items` (`serviceId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `service_order_products` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `serviceOrderItemId` INTEGER NOT NULL, `productId` INTEGER NOT NULL, `productName` TEXT NOT NULL, `productUnitMeasure` TEXT NOT NULL, `quantity` REAL NOT NULL, FOREIGN KEY(`serviceOrderItemId`) REFERENCES `service_order_items`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`productId`) REFERENCES `products`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_order_products_serviceOrderItemId` ON `service_order_products` (`serviceOrderItemId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_service_order_products_productId` ON `service_order_products` (`productId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a4b46f589406c181245b53aafa233077')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `clients`");
        db.execSQL("DROP TABLE IF EXISTS `services`");
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `service_orders`");
        db.execSQL("DROP TABLE IF EXISTS `service_order_items`");
        db.execSQL("DROP TABLE IF EXISTS `service_order_products`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsClients = new HashMap<String, TableInfo.Column>(7);
        _columnsClients.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("cpfCnpj", new TableInfo.Column("cpfCnpj", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsClients.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysClients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesClients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoClients = new TableInfo("clients", _columnsClients, _foreignKeysClients, _indicesClients);
        final TableInfo _existingClients = TableInfo.read(db, "clients");
        if (!_infoClients.equals(_existingClients)) {
          return new RoomOpenHelper.ValidationResult(false, "clients(com.rfpiscinas.serviceorder.data.local.entity.ClientEntity).\n"
                  + " Expected:\n" + _infoClients + "\n"
                  + " Found:\n" + _existingClients);
        }
        final HashMap<String, TableInfo.Column> _columnsServices = new HashMap<String, TableInfo.Column>(5);
        _columnsServices.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServices.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServices.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServices.put("usesProducts", new TableInfo.Column("usesProducts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServices.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysServices = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesServices = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoServices = new TableInfo("services", _columnsServices, _foreignKeysServices, _indicesServices);
        final TableInfo _existingServices = TableInfo.read(db, "services");
        if (!_infoServices.equals(_existingServices)) {
          return new RoomOpenHelper.ValidationResult(false, "services(com.rfpiscinas.serviceorder.data.local.entity.ServiceEntity).\n"
                  + " Expected:\n" + _infoServices + "\n"
                  + " Found:\n" + _existingServices);
        }
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(4);
        _columnsProducts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("unitMeasure", new TableInfo.Column("unitMeasure", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.rfpiscinas.serviceorder.data.local.entity.ProductEntity).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(8);
        _columnsUsers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.rfpiscinas.serviceorder.data.local.entity.UserEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsServiceOrders = new HashMap<String, TableInfo.Column>(10);
        _columnsServiceOrders.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("clientId", new TableInfo.Column("clientId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("clientName", new TableInfo.Column("clientName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("clientAddress", new TableInfo.Column("clientAddress", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("employeeId", new TableInfo.Column("employeeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("employeeName", new TableInfo.Column("employeeName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("startDateTime", new TableInfo.Column("startDateTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("endDateTime", new TableInfo.Column("endDateTime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrders.put("synced", new TableInfo.Column("synced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysServiceOrders = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysServiceOrders.add(new TableInfo.ForeignKey("clients", "CASCADE", "NO ACTION", Arrays.asList("clientId"), Arrays.asList("id")));
        _foreignKeysServiceOrders.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("employeeId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesServiceOrders = new HashSet<TableInfo.Index>(2);
        _indicesServiceOrders.add(new TableInfo.Index("index_service_orders_clientId", false, Arrays.asList("clientId"), Arrays.asList("ASC")));
        _indicesServiceOrders.add(new TableInfo.Index("index_service_orders_employeeId", false, Arrays.asList("employeeId"), Arrays.asList("ASC")));
        final TableInfo _infoServiceOrders = new TableInfo("service_orders", _columnsServiceOrders, _foreignKeysServiceOrders, _indicesServiceOrders);
        final TableInfo _existingServiceOrders = TableInfo.read(db, "service_orders");
        if (!_infoServiceOrders.equals(_existingServiceOrders)) {
          return new RoomOpenHelper.ValidationResult(false, "service_orders(com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderEntity).\n"
                  + " Expected:\n" + _infoServiceOrders + "\n"
                  + " Found:\n" + _existingServiceOrders);
        }
        final HashMap<String, TableInfo.Column> _columnsServiceOrderItems = new HashMap<String, TableInfo.Column>(7);
        _columnsServiceOrderItems.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("serviceOrderId", new TableInfo.Column("serviceOrderId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("serviceId", new TableInfo.Column("serviceId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("serviceName", new TableInfo.Column("serviceName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("serviceType", new TableInfo.Column("serviceType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("serviceUsesProducts", new TableInfo.Column("serviceUsesProducts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderItems.put("observations", new TableInfo.Column("observations", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysServiceOrderItems = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysServiceOrderItems.add(new TableInfo.ForeignKey("service_orders", "CASCADE", "NO ACTION", Arrays.asList("serviceOrderId"), Arrays.asList("id")));
        _foreignKeysServiceOrderItems.add(new TableInfo.ForeignKey("services", "CASCADE", "NO ACTION", Arrays.asList("serviceId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesServiceOrderItems = new HashSet<TableInfo.Index>(2);
        _indicesServiceOrderItems.add(new TableInfo.Index("index_service_order_items_serviceOrderId", false, Arrays.asList("serviceOrderId"), Arrays.asList("ASC")));
        _indicesServiceOrderItems.add(new TableInfo.Index("index_service_order_items_serviceId", false, Arrays.asList("serviceId"), Arrays.asList("ASC")));
        final TableInfo _infoServiceOrderItems = new TableInfo("service_order_items", _columnsServiceOrderItems, _foreignKeysServiceOrderItems, _indicesServiceOrderItems);
        final TableInfo _existingServiceOrderItems = TableInfo.read(db, "service_order_items");
        if (!_infoServiceOrderItems.equals(_existingServiceOrderItems)) {
          return new RoomOpenHelper.ValidationResult(false, "service_order_items(com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderItemEntity).\n"
                  + " Expected:\n" + _infoServiceOrderItems + "\n"
                  + " Found:\n" + _existingServiceOrderItems);
        }
        final HashMap<String, TableInfo.Column> _columnsServiceOrderProducts = new HashMap<String, TableInfo.Column>(6);
        _columnsServiceOrderProducts.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderProducts.put("serviceOrderItemId", new TableInfo.Column("serviceOrderItemId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderProducts.put("productId", new TableInfo.Column("productId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderProducts.put("productName", new TableInfo.Column("productName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderProducts.put("productUnitMeasure", new TableInfo.Column("productUnitMeasure", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsServiceOrderProducts.put("quantity", new TableInfo.Column("quantity", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysServiceOrderProducts = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysServiceOrderProducts.add(new TableInfo.ForeignKey("service_order_items", "CASCADE", "NO ACTION", Arrays.asList("serviceOrderItemId"), Arrays.asList("id")));
        _foreignKeysServiceOrderProducts.add(new TableInfo.ForeignKey("products", "CASCADE", "NO ACTION", Arrays.asList("productId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesServiceOrderProducts = new HashSet<TableInfo.Index>(2);
        _indicesServiceOrderProducts.add(new TableInfo.Index("index_service_order_products_serviceOrderItemId", false, Arrays.asList("serviceOrderItemId"), Arrays.asList("ASC")));
        _indicesServiceOrderProducts.add(new TableInfo.Index("index_service_order_products_productId", false, Arrays.asList("productId"), Arrays.asList("ASC")));
        final TableInfo _infoServiceOrderProducts = new TableInfo("service_order_products", _columnsServiceOrderProducts, _foreignKeysServiceOrderProducts, _indicesServiceOrderProducts);
        final TableInfo _existingServiceOrderProducts = TableInfo.read(db, "service_order_products");
        if (!_infoServiceOrderProducts.equals(_existingServiceOrderProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "service_order_products(com.rfpiscinas.serviceorder.data.local.entity.ServiceOrderProductEntity).\n"
                  + " Expected:\n" + _infoServiceOrderProducts + "\n"
                  + " Found:\n" + _existingServiceOrderProducts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a4b46f589406c181245b53aafa233077", "e3486fe8edf26b429b0b45b25d8a752a");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "clients","services","products","users","service_orders","service_order_items","service_order_products");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `clients`");
      _db.execSQL("DELETE FROM `services`");
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `service_orders`");
      _db.execSQL("DELETE FROM `service_order_items`");
      _db.execSQL("DELETE FROM `service_order_products`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ClientDao.class, ClientDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ServiceDao.class, ServiceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ServiceOrderDao.class, ServiceOrderDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ClientDao clientDao() {
    if (_clientDao != null) {
      return _clientDao;
    } else {
      synchronized(this) {
        if(_clientDao == null) {
          _clientDao = new ClientDao_Impl(this);
        }
        return _clientDao;
      }
    }
  }

  @Override
  public ServiceDao serviceDao() {
    if (_serviceDao != null) {
      return _serviceDao;
    } else {
      synchronized(this) {
        if(_serviceDao == null) {
          _serviceDao = new ServiceDao_Impl(this);
        }
        return _serviceDao;
      }
    }
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public ServiceOrderDao serviceOrderDao() {
    if (_serviceOrderDao != null) {
      return _serviceOrderDao;
    } else {
      synchronized(this) {
        if(_serviceOrderDao == null) {
          _serviceOrderDao = new ServiceOrderDao_Impl(this);
        }
        return _serviceOrderDao;
      }
    }
  }
}
