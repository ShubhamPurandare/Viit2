package college.root.viit2.Realm;

import android.util.Log;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;
import io.realm.internal.Table;

/**
 * Created by root on 25/11/16.
 */

public class RealmHelper {

    Realm realm;
    RealmResults<Data> results = null;
    Boolean saved = null;
    Boolean saved1 = null;
    Boolean saved2 = null;
    RealmQuery<Data> queryData = null;
    String TAG = "test";
    RealmResults<UserInfo> results2;
    UserInfo info;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }


    public Data retriveFromPid(int pid){
        Log.d(TAG, "retriveFromPid: method called");
        Data data  = realm.where(Data.class).equalTo("postid" , pid).findFirst();

        return data;

    }


    public boolean save(final Data data) {

        if (data == null) {
            saved = false;

        } else {


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        Data d = realm.copyToRealm(data);
                        saved = true;



                    } catch (RealmException r) {
                        saved = false;
                    }
                }
            });


        }

        return saved;
    }


    public void retrive() {

        results = realm.where(Data.class).findAll();
        results.sort("postid", Sort.DESCENDING);


    }


    public ArrayList<Data> refresh() {

        ArrayList<Data> list = new ArrayList<>();
        // results.sort();
        for (Data data : results) {
            list.add(data);

        }
        return list;

    }


    public boolean check( int pid) {
        results = null;
        queryData = null;
        queryData = realm.where(Data.class);
        queryData.equalTo("postid" , pid);
        results = realm.where(Data.class).findAllSorted("postid");
        for (int i = 0 ; i< results.size() ; i++ ){
            Log.d("Test", "check: "+ results.get(i));
        }
     //   results = realm.where(Data.class).equalTo("postid" , pid).findAll();

        //  pid = results.first();
        if (queryData!=null){
            Log.d(TAG, "check: query contain " );

            return true; // post id already exists
        }else {
            Log.d(TAG, "check: results contain a null value is not present in database ....");
            return  false; // new post
        }
    }


    // Code below is for PerceptionData








    public boolean saveUserInfo(final  UserInfo userInfo){
        if (userInfo == null) {
            saved2 = false;

        } else {


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        UserInfo userInfo1 = realm.copyToRealm(userInfo);
                        saved2 = true;

                    } catch (RealmException r) {
                        saved2 = false;
                    }
                }
            });


        }

        return saved2;

    }

    public UserInfo getUserInfo(){
         info = new UserInfo();
        results2 = null;
        info = realm.where(UserInfo.class).findFirst();
        return  info;
    }

    public void updateData(final Data data){
        realm.beginTransaction();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(data);
                realm.commitTransaction();
            }
        });
    }



}
