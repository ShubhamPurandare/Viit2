package college.root.viit2.Realm;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.exceptions.RealmException;

/**
 * Created by root on 25/11/16.
 */

public class RealmHelper {

    Realm realm;
    RealmResults<Data> results;
    Boolean saved = null;

    public RealmHelper(Realm realm) {
        this.realm = realm;
    }



    public  boolean save (final Data data) {

        if(data == null){
            saved = false;

        }else{


            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {



                    try{

                        Data d = realm.copyToRealm(data);
                        saved = true;



                    }catch (RealmException r){
                        saved= false;
                    }
                }
            });


        }

        return  saved;
    }

    public int getLatestPid(){

        int pid = 0;
        results = realm.where(Data.class).findAll();
        results.sort("postid" , Sort.DESCENDING);

      //  pid = results.first();

        return  pid;
    }


    public  void retrive(){

        results = realm.where(Data.class).findAll();
        results.sort("postid", Sort.DESCENDING);

    }


    public ArrayList<Data> refresh(){

        ArrayList<Data> list = new ArrayList<>();
      // results.sort();
        for (Data data : results){
            list.add(data);

        }
        return list;

    }
}
