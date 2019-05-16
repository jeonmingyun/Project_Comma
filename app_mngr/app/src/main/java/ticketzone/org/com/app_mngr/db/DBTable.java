package ticketzone.org.com.app_mngr.db;

import android.provider.BaseColumns;

public final class DBTable {

    /*점주*/
    public static final class Owner implements  BaseColumns{
        public static final String OWNER_ID = "owner_id";
        public static final String TABLENAME = "owner";
        public static final String CREATE_QUERY = "create table IF NOT EXISTS " + TABLENAME + "("
                + OWNER_ID + " text primary key);";
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }

    /*매장*/
    public static final class Store implements BaseColumns{
        public static final String LICENSE_NUMBER = "license_number";
        public static final String R_NAME = "r_name";
        public static final String MAX_NUMBER = "max_number";
        public static final String STORE_STATUS = "store_status";
        public static final String CATE_CODE = "cate_code";
        public static final String OWNER_ID = "owner_id";
        public static final String STORE_NAME = "store_name";
        public static final String STORE_TEL = "store_tel";
        public static final String STORE_TIME = "store_time";
        public static final String STORE_INTRO = "store_intro";
        public static final String ADDRESS_NAME = "address_name";
        public static final String TABLENAME = "store";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + LICENSE_NUMBER + " text primary key,"
                + R_NAME + " text not null,"
                + MAX_NUMBER + " text,"
                + STORE_STATUS + " integer default 0,"
                + CATE_CODE + " text,"
                + OWNER_ID + " text,"
                + STORE_NAME + " text,"
                + STORE_TEL + " text not null unique,"
                + STORE_TIME + " text not null,"
                + STORE_INTRO + " text not null,"
                + ADDRESS_NAME + " text not null,"
                + "foreign key("+OWNER_ID+") references OWNER("+OWNER_ID+"),"
                + " foreign key("+CATE_CODE+") references CATEGORIE("+CATE_CODE+"));";
        public static final String DROP_QUERY= "drop table if exists " + TABLENAME;
    }

    /*메뉴*/
    public static final class StoreMenu implements  BaseColumns{
        public static final String MENU_CODE = "menu_code";
        public static final String MENU_NAME = "menu_name";
        public static final String MENU_PRICE = "menu_price";
        public static final String STORE_NOTE = "store_note";
        public static final String TABLENAME = "store_menu";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + MENU_CODE + " text primary key,"
                + MENU_NAME + " text not null,"
                + MENU_PRICE + " text not null,"
                + STORE_NOTE + " text );";
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }

    /*카테고리*/
    public static final class Categorie implements BaseColumns{
        public static final String CATE_CODE = "cate_code";
        public static final String CATE_NAME = "cate_name";
        public static final String TABLENAME = "categorie";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + CATE_CODE + " text primary key,"
                + CATE_NAME + " text not null);";
        public static final String DROP_QUERY = "drop table if exists "+ TABLENAME;
    }
}
