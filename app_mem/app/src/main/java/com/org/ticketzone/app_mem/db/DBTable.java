package com.org.ticketzone.app_mem.db;

import android.provider.BaseColumns;

public final class DBTable {

    /*회원*/
    public static final class Member implements BaseColumns {
        public static final String MEMBER_ID = "member_id"; // tel
        public static final String MEMBER_NICKNAME = "member_nickname"; // name
        public static final String MEMBER_BIRTH= "member_birth";
        public static final String MEMBER_GENDER= "member_gender";
        public static final String MEMBER_AGE_RANGE= "member_age_range";
        public static final String TABLENAME= "member";
        public static final String CREATE_QUERY= "create table IF NOT EXISTS "+ TABLENAME + "("
                + MEMBER_ID + " text primary key,"
                + MEMBER_NICKNAME + " text not null,"
                + MEMBER_BIRTH + " text,"
                + MEMBER_GENDER + " text,"
                + MEMBER_AGE_RANGE + " text );";
        public static final String DROP_QUERY= "drop table if exists "+TABLENAME;
    }

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
        public static final String STORE_TEL = "store_tel";
        public static final String STORE_TIME = "store_time";
        public static final String STORE_NAME = "store_name";
        public static final String STORE_INTRO = "store_intro";
        public static final String ADDRESS_NAME = "address_name";
        public static final String IMG_UUID = "img_uuid";
        public static final String IMG_UPLOADPATH = "img_uploadpath";
        public static final String IMG_FILENAME = "img_filename";
        public static final String TABLENAME = "store";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + LICENSE_NUMBER + " text primary key,"
                + R_NAME + " text not null,"
                + MAX_NUMBER + " text,"
                + STORE_STATUS + " integer default 0,"
                + CATE_CODE + " text,"
                + OWNER_ID + " text,"
                + STORE_TEL + " text not null unique,"
                + STORE_TIME + " text,"
                + STORE_NAME + " text not null,"
                + STORE_INTRO + " text,"
                + IMG_UUID + " text, "
                + IMG_UPLOADPATH + " text, "
                + IMG_FILENAME + " text, "
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

    /*좌표*/
    public static final class Coordinates implements BaseColumns{
        public static final String COOR_X = "coor_x";
        public static final String COOR_Y = "coor_y";
        public static final String LICENSE_NUMBER = "license_number";
        public static final String TABLENAME = "coordinates";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + COOR_X + " text not null,"
                + COOR_Y + " text not null,"
                + LICENSE_NUMBER + " text,"
                + " foreign key("+LICENSE_NUMBER+") references STORE("+LICENSE_NUMBER+"));";
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }

    /*번호표*/
    public static final class NumberTicket implements BaseColumns{
        public static final String TICKET_CODE = "ticket_code";
        public static final String WAIT_NUMBER = "wait_number";
        public static final String THE_NUMBER = "the_number";
        public static final String LICENSE_NUMBER = "license_number";
        public static final String MEMBER_ID = "member_id";
        public static final String TICKET_STATUS = "ticket_status";
        public static final String TABLENAME = "numberticket";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + TICKET_CODE + " text primary key,"
                + WAIT_NUMBER + " integer not null,"
                + THE_NUMBER + " integer not null,"
                + LICENSE_NUMBER + " text,"
                + MEMBER_ID + " text,"
                + TICKET_STATUS + " text);";         
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }
    /*비콘*/
    public static final class Beacon implements BaseColumns{
        public static final String B_CODE = "b_code";
        public static final String STORE_NAME = "store_name";
        public static final String LICENSE_NUMBER = "license_number";
        public static final String TABLENAME = "beacon";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + B_CODE + " text primary key,"
                + STORE_NAME + " text ,"
                + LICENSE_NUMBER + " text)";
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }
    /*GPS*/
    public static final class GpsTest implements BaseColumns{
        public static final String COOR_X = "coor_x";
        public static final String COOR_Y = "coor_y";
        public static final String DISTANCE = "distance";
        public static final String STORE_NAME = "store_name";
        public static final String TABLENAME = "gpstest";
        public static final String CREATE_QUERY = "create table " + TABLENAME + "("
                + COOR_X + " text ,"
                + COOR_Y + " text ,"
                + DISTANCE + " text ,"
                + STORE_NAME + " text)";
        public static final String DROP_QUERY = "drop table if exists " + TABLENAME;
    }


}