package info.androidhive.barcodereader.SQLiteDatabaseFolder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.barcodereader.MonthReportItem;
import info.androidhive.barcodereader.MonthlyItem;
import info.androidhive.barcodereader.Product;
import info.androidhive.barcodereader.Purchase;
import info.androidhive.barcodereader.Sale;
import info.androidhive.barcodereader.Stock;
import info.androidhive.barcodereader.User;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "getcontact_dbase";

    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCT = "products";
    private static final String TABLE_SALES = "sales";
    private static final String TABLE_PURCHASE = "purchases";
    private static final String TABLE_STOCKS = "stocks";

    private static final String KEY_USERS_ID = "id";
    private static final String KEY_USERS_USERNAME = "username";
    private static final String KEY_USERS_PASSWORD = "password";

    private static final String KEY_SALE_ID = "id";
    private static final String KEY_SALE_DATE = "date";
    private static final String KEY_SALE_PRODUCT_NAME = "product_name";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_QUANTITY = "quantity";

    private static final String KEY_PRODUCT_ID = "id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_BARCODE = "barcode";

    private static final String KEY_PURCHASE_ID = "id";
    private static final String KEY_PURCHASE_DATE = "date";
    private static final String KEY_PURCHASE_NAME = "purchase_name";
    private static final String KEY_PURCHASE_AMOUNT = "amount";

    private static final String KEY_STOCK_ID = "stock_id";
    private static final String KEY_STOCK_PRODUCT_ID = "product_id";
    private static final String KEY_STOCK_QUANTITY = "quantity";
    private static final String KEY_STOCK_BARCODE = "barcode";

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_USERS_ID + " INTEGER PRIMARY KEY," + KEY_USERS_USERNAME + " TEXT,"
            + KEY_USERS_PASSWORD + " TEXT" + ")";

    String CREATE_SALES_TABLE = "CREATE TABLE " + TABLE_SALES + "("
            + KEY_SALE_ID + " INTEGER PRIMARY KEY," + KEY_SALE_DATE + " DATE,"
            + KEY_SALE_PRODUCT_NAME + " TEXT," + KEY_AMOUNT + " DOUBLE," + KEY_QUANTITY + " INTEGER" + ")";

    String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
            + KEY_PRODUCT_ID + " INTEGER PRIMARY KEY," + KEY_PRODUCT_NAME + " TEXT," + KEY_BARCODE + " TEXT" + ")";

    String CREATE_PURCHASE_TABLE = "CREATE TABLE " + TABLE_PURCHASE + "("
            + KEY_PURCHASE_ID + " INTEGER PRIMARY KEY," + KEY_PURCHASE_DATE + " DATE," + KEY_PURCHASE_NAME + " TEXT,"
            + KEY_PURCHASE_AMOUNT + " DOUBLE" + ")";

    String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCKS + "("
            + KEY_STOCK_ID + " INTEGER PRIMARY KEY," + KEY_STOCK_QUANTITY + " INTEGER," + KEY_STOCK_BARCODE + " TEXT," + KEY_STOCK_PRODUCT_ID + " INTEGER, CONSTRAINT fk_productID FOREIGN KEY (product_id) REFERENCES products(id) " + ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_TABLE);
        db.execSQL(CREATE_SALES_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PURCHASE_TABLE);
        db.execSQL(CREATE_STOCK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCKS);

        // Create tables again
        onCreate(db);
    }

    public void deleteTableUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.close();
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERS_USERNAME, user.getUsername());
        values.put(KEY_USERS_PASSWORD, user.getPassword());

        boolean success = false;

        try {
            db.insert(TABLE_USERS, null, values);
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;

    }

//    public void addSentMsg(SentMessage sentMessage){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_SENT_MSG_TO,sentMessage.getSentTo());
//        values.put(KEY_SENT_MSG_NUMBER,sentMessage.getPhoneNumber());
//        values.put(KEY_SENT_MSG_DATE,sentMessage.getDate());
//
//        db.insert(TABLE_SENT_MSG,null,values);
//        db.close();
//    }

    public User userLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE username='" + username + "' AND password='" + password + "'", null);

        if (cursor.moveToFirst()) {
            System.out.println("===========================================================" + cursor.getString(1));
            cursor.moveToFirst();
            User user = new User(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2));
            cursor.close();
            return user;
        } else {
            return null;
        }
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USERS_ID, KEY_USERS_USERNAME, KEY_USERS_PASSWORD},
                KEY_USERS_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        User user = new User(cursor.getString(0), cursor.getString(1),
                cursor.getString(2));
        cursor.close();
        return user;
    }

    public int getQuantity(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM STOCKS WHERE barcode='" + barcode + "'", null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() > 0){
            int quantity = cursor.getInt(1);
            cursor.close();

            return quantity;
        }else{
            return 0;
        }


    }

//    public  SentMessage getSentMessage(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_SENT_MSG,new String[] {KEY_SENT_MSG_ID, KEY_SENT_MSG_TO, KEY_SENT_MSG_NUMBER, KEY_SENT_MSG_DATE},
//                KEY_SENT_MSG_ID + "=?",new String[] {String.valueOf(id)}, null,null,null,null);
//        if(cursor != null){
//            cursor.moveToFirst();
//        }
//
//        return new SentMessage(Integer.parseInt(cursor.getString(0)),cursor.getString(1),
//                cursor.getString(2), Long.parseLong(cursor.getString(3)));
//    }

//    public List<User> getAllUsers(){
//        List<User> userList = new ArrayList<>();
//
//        String selectQuery = "SELECT * FROM " + TABLE_USERS;
//
//        SQLiteDatabase db  = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery,null);
//
//        if (cursor.moveToFirst()){
//            do {
//                User user = new User();
//                user.setId(Integer.parseInt(cursor.getString(0)));
//                user.setUsername(cursor.getString(1));
//                user.setPassword(cursor.getString(2));
//                user.setGender(cursor.getString(3));
//                userList.add(user);
//            }while (cursor.moveToNext());
//        }
//
//        cursor.close();
//
//        return userList;
//    }

    public boolean addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProduct_name());
        values.put(KEY_BARCODE, product.getBarcode());

//        ArrayList<Product> products = new ArrayList<>();
//        int product_id = 0;
//        String product_barcode = "";
//
//        products = getAllProducts();

//        ContentValues valuesStock = new ContentValues();
//        valuesStock.put(KEY_STOCK_PRODUCT_ID, product.getId());
//        valuesStock.put(KEY_STOCK_QUANTITY, startquantity);
//        valuesStock.put(KEY_STOCK_BARCODE, product.getBarcode());
//
//        System.out.println("----------------------------------------------" + product.getId() + "  " + startquantity + "  " + product.getBarcode());

        boolean success = false;

        try {
            db.insert(TABLE_PRODUCT, null, values);
//            db.insert(TABLE_STOCKS, null, valuesStock);

            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }

    public boolean addPurchase(Purchase purchase){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        ContentValues valuesPurchase = new ContentValues();
        valuesPurchase.put(KEY_PURCHASE_DATE, purchase.getDate());
        valuesPurchase.put(KEY_PURCHASE_AMOUNT, purchase.getAmount());
        valuesPurchase.put(KEY_PURCHASE_NAME, purchase.getName());

        ArrayList<Product> products = new ArrayList<>();
        int product_id = 0;
        String product_barcode = "";

        products = getAllProducts();

        for(Product product: products){
            if(product.getProduct_name().equals(purchase.getName())){
                product_id = product.getId();
                product_barcode = product.getBarcode();
            }
        }

        ContentValues valuesStock = new ContentValues();
        valuesStock.put(KEY_STOCK_PRODUCT_ID, product_id);
        valuesStock.put(KEY_STOCK_QUANTITY, purchase.getQuantity());
        valuesStock.put(KEY_STOCK_BARCODE, product_barcode);

        try{
            db.insert(TABLE_PURCHASE, null, valuesPurchase);
            db.insert(TABLE_STOCKS, null, valuesStock);
            success = true;
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.close();
        }

        return success;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//                sale.setId(Integer.parseInt(cursor.getString(0)));
//                sale.setProduct_name(cursor.getString(2));
//                sale.setAmount(Double.parseDouble(cursor.getString(3)));
//                sale.setDate(Long.parseLong(cursor.getString(1)));
                System.out.println(cursor.getString(2));
                products.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return products;
    }

    public ArrayList<Stock> getAllStocks() {
        ArrayList<Stock> stocks = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Stock stock = new Stock(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3), cursor.getString(2));
//                sale.setId(Integer.parseInt(cursor.getString(0)));
//                sale.setProduct_name(cursor.getString(2));
//                sale.setAmount(Double.parseDouble(cursor.getString(3)));
//                sale.setDate(Long.parseLong(cursor.getString(1)));
                stocks.add(stock);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return stocks;
    }

    public Stock getStock(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM STOCKS WHERE barcode='" + barcode + "'", null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Stock stock = new Stock(cursor.getInt(0), cursor.getInt(1), cursor.getInt(3), cursor.getString(2));
        cursor.close();

        return stock;
    }

    public Product getProduct(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PRODUCTS WHERE barcode='" + barcode + "'", null);

        Product product;

        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        if (cursor.getCount() > 0)
            product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
        else
            product = null;

        cursor.close();

        return product;
    }

    public boolean addSale(String barcode, double amount, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        List<Product> productList = new ArrayList<>();
        Sale sale = new Sale();

        long time = new Date().getTime();

        productList = getAllProducts();

        for (Product product : productList) {
            if (product.getBarcode().equals(barcode)) {
                sale.setAmount(amount);
                sale.setDate(simpleDateFormat.format(time));
                sale.setProduct_name(product.getProduct_name());
                sale.setQuantity(quantity);
                break;
            }
        }

        int initialQuantity = getStock(barcode).getQuantity();

        boolean success = false;

        try {

            if (!sale.getProduct_name().equals("")) {
                ContentValues values = new ContentValues();
                values.put(KEY_SALE_PRODUCT_NAME, sale.getProduct_name());
                values.put(KEY_AMOUNT, sale.getAmount());
                values.put(KEY_SALE_DATE, sale.getDate());
                values.put(KEY_QUANTITY, sale.getQuantity());

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + sale.getDate());

                db.insert(TABLE_SALES, null, values);
                db.execSQL("UPDATE stocks SET quantity='" + (initialQuantity - quantity) + "' WHERE barcode='" + barcode + "'");
                success = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }

    public ArrayList<Sale> getAllSales() {
        ArrayList<Sale> sales = new ArrayList<>();

//        String selectQuery = "SELECT * FROM " + TABLE_SALES + "," + TABLE_PRODUCT + " ORDER BY date DESC";
        String selectQuery = "SELECT * FROM " + TABLE_SALES + " ORDER BY date DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Sale sale = new Sale();
                sale.setId(cursor.getInt(0));
                sale.setProduct_name(cursor.getString(2));
                sale.setAmount(Double.parseDouble(cursor.getString(3)));
                sale.setDate(cursor.getString(1));
                sale.setQuantity(cursor.getInt(4));
                sales.add(sale);

                System.out.println("==========================================================================" + sale.getProduct_name());
                System.out.println("--------------------------------------------------------------------------" + sale.getId());
                System.out.println("--------------------------------------------------------------------------" + sale.getDate());
                System.out.println("--------------------------------------------------------------------------" + simpleDateFormat.format(new Date().getTime()));
            } while (cursor.moveToNext());
        }

//        System.out.println("===============================================" + "the size of the list is: " + productList.size());

        cursor.close();

        return sales;
    }

    public ArrayList<Purchase> getAllPurchases() {
        ArrayList<Purchase> purchases = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_PURCHASE + " ORDER BY date DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(4), cursor.getDouble(3));
                purchases.add(purchase);
            } while (cursor.moveToNext());
        }

        return purchases;
    }

    public ArrayList<MonthReportItem> getMonthlyReportItems() {
        ArrayList<MonthlyItem> sales = new ArrayList<>();
        ArrayList<MonthlyItem> purchases = new ArrayList<>();
        ArrayList<MonthReportItem> monthReportItems = new ArrayList<>();

        String salesQuery = "SELECT sum(amount) as saleamount, strftime('%m', date) as monthname, strftime('%Y', date) as yearname FROM sales GROUP BY strftime('%m', date) ORDER BY strftime('%Y', date) desc,strftime('%m', date) desc";
        String purchaseQuery = "SELECT sum(amount) as purchaseamount, strftime('%m',date) as monthname, strftime('%Y', date) as yearname FROM purchases GROUP BY strftime('%m', date) ORDER BY strftime('%Y', date) desc,strftime('%m', date)";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(salesQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MonthlyItem sale = new MonthlyItem(cursor.getDouble(0), cursor.getString(1) + "/" + cursor.getString(2));
                sales.add(sale);
            } while (cursor.moveToNext());
        }

        Cursor cursor2 = db.rawQuery(purchaseQuery, null);

        if (cursor2.moveToFirst()) {
            do {
                MonthlyItem purchase = new MonthlyItem(cursor2.getDouble(0), cursor2.getString(1) + "/" + cursor2.getString(2));
                purchases.add(purchase);
            } while (cursor2.moveToNext());
        }

        if (sales.size() == purchases.size()) {
            for (int i = 0; i < sales.size(); i++) {
                monthReportItems.add(new MonthReportItem(sales.get(i).getMonthYear(), sales.get(i).getAmount(), purchases.get(i).getAmount(), sales.get(i).getAmount() - purchases.get(i).getAmount()));
            }
        } else {
            if (purchases.size() > sales.size()) {
                for (int i = 0; i < purchases.size(); i++) {
                    if (i >= sales.size()) {
                        monthReportItems.add(new MonthReportItem(purchases.get(i).getMonthYear(), 0, purchases.get(i).getAmount(), 0 - purchases.get(i).getAmount()));
                    } else {
                        monthReportItems.add(new MonthReportItem(sales.get(i).getMonthYear(), sales.get(i).getAmount(), purchases.get(i).getAmount(), sales.get(i).getAmount() - purchases.get(i).getAmount()));
                    }
                }
            } else {
                for (int i = 0; i < sales.size(); i++) {
                    if (i >= purchases.size()) {
                        monthReportItems.add(new MonthReportItem(sales.get(i).getMonthYear(), sales.get(i).getAmount(), 0, sales.get(i).getAmount() - 0));
                    } else {
                        monthReportItems.add(new MonthReportItem(sales.get(i).getMonthYear(), sales.get(i).getAmount(), purchases.get(i).getAmount(), sales.get(i).getAmount() - purchases.get(i).getAmount()));
                    }
                }
            }
        }

        return monthReportItems;
    }

    public MonthReportItem getDailyReport(String date) {
        double salesTotal = 0.0;
        double purchaseTotal = 0.0;
        String dt = "";

        System.out.println("000000000000000000000000000000000  " + date);

        String salesQuery = "SELECT sum(amount) as saleamount, date FROM sales WHERE date='" + date + "'";
        String purchaseQuery = "SELECT sum(amount) as purchaseamount, date FROM purchases WHERE date='" + date + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(salesQuery, null);

        if (cursor != null)
            if (cursor.moveToFirst()) {
                do {
//                    MonthlyItem monthlyItem = new MonthlyItem(cursor.getDouble(0), cursor.getString(1));
//                    sales.add(monthlyItem);
                    System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," + cursor.getDouble(0) + " " + cursor.getString(1));
                    salesTotal = salesTotal + cursor.getDouble(0);
                } while (cursor.moveToNext());
            }

        Cursor cursor2 = db.rawQuery(purchaseQuery, null);

        if (cursor2 != null)
            if (cursor2.moveToFirst()) {
                do {
//                    MonthlyItem monthlyItem = new MonthlyItem(cursor2.getDouble(0), cursor2.getString(1));
//                    purchases.add(monthlyItem);
                    System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,," + cursor2.getDouble(0) + " " + cursor2.getString(1));
                    purchaseTotal = purchaseTotal + cursor2.getDouble(0);
                } while (cursor2.moveToNext());
            }

        cursor.close();
        cursor2.close();

        return new MonthReportItem(date, salesTotal, purchaseTotal, salesTotal - purchaseTotal);
    }

    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            db.delete(TABLE_PRODUCT, KEY_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.delete(TABLE_STOCKS, KEY_STOCK_ID + " = ?",
                    new String[]{String.valueOf(id)});
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }

    public boolean deleteSale(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        try {
            db.delete(TABLE_SALES, KEY_SALE_DATE + " = ?",
                    new String[]{String.valueOf(id)});
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return success;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProduct_name());

        return db.update(TABLE_PRODUCT, values, KEY_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public int updateSale(Sale sale) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SALE_PRODUCT_NAME, sale.getProduct_name());
        values.put(KEY_SALE_DATE, sale.getDate());
        values.put(KEY_QUANTITY, sale.getQuantity());
        values.put(KEY_AMOUNT, sale.getAmount());

        return db.update(TABLE_SALES, values, KEY_SALE_ID + " = ?",
                new String[]{String.valueOf(sale.getId())});
    }

//    public int getSentMessageCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_SENT_MSG;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery,null);
//        cursor.close();
//        return cursor.getCount();
//    }

//    public int updateUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_USERS_USERNAME,user.getUsername());
//        values.put(KEY_USERS_GENDER,user.getGender());
//        values.put(KEY_USERS_PASSWORD,user.getPassword());
//
//        return db.update(TABLE_USERS,values, KEY_USERS_ID + " = ?",
//                new String[] { String.valueOf(user.getId()) });
//    }
//
//    public int updateSentMessage(SentMessage sentMessage) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_SENT_MSG_TO,sentMessage.getSentTo());
//        values.put(KEY_SENT_MSG_NUMBER,sentMessage.getPhoneNumber());
//        values.put(KEY_SENT_MSG_DATE,sentMessage.getDate());
//
//        return db.update(TABLE_SENT_MSG,values, KEY_USERS_ID + " = ?",
//                new String[] {String.valueOf(sentMessage.getId())});
//    }
//
//    public void deleteUser(User user) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_USERS, KEY_USERS_ID + " = ?",
//                new String[] { String.valueOf(user.getId()) });
//        db.close();
//    }
//
//    public void deleteSentMessage(SentMessage sentMessage) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_SENT_MSG, KEY_SENT_MSG_ID + " = ?",
//                new String[] { String.valueOf(sentMessage.getId()) });
//        db.close();
//    }
//
//    public void deleteAllMessages(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        for(SentMessage sentMessage: getAllSentMessages()){
//        db.delete(TABLE_SENT_MSG,KEY_SENT_MSG_ID + " = ?",new String[] { String.valueOf(sentMessage.getId())});
//        }
//    }
}
