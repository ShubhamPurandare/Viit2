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
    RealmResults<PerceptionData> Presults = null;
    RealmQuery<PerceptionData> query = null;
    RealmQuery<Data> queryData = null;
    String TAG = "test";

    public RealmHelper(Realm realm) {
        this.realm = realm;
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

    public int getLatestPid() {

        int pid = 0;
        results = realm.where(Data.class).findAll();
        results.sort("postid", Sort.DESCENDING);

        //  pid = results.first();

        return pid;
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


    public boolean save(final PerceptionData pdata) {

        if (pdata == null) {
            saved = false;
        } else{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {


                    try {

                        PerceptionData d = realm.copyToRealm(pdata);
                        saved1 = true;


                    } catch (RealmException r) {
                        saved1 = false;
                    }
                }
            });
        }

        return saved1;
    }


    public void retrivePerception() {

        Presults = realm.where(PerceptionData.class).findAll();
        Presults.sort("postid", Sort.DESCENDING);

    }


    public ArrayList<PerceptionData> refreshPerception() {

        ArrayList<PerceptionData> list = new ArrayList<>();
        // results.sort();
        for (PerceptionData pdata : Presults) {
            list.add(pdata);

        }
        return list;

    }

    public boolean checkPidExists( int pid) {

        query = realm.where(PerceptionData.class);
        query.equalTo("postid" , pid);
        Presults = realm.where(PerceptionData.class).equalTo("postid" , pid).findAll();


        //  pid = results.first();
        if (Presults!=null){
            return true;
        }else {
            return  false;
        }
    }


}
