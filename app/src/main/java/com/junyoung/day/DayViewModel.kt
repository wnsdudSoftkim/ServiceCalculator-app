package com.junyoung.day

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DayViewModel : ViewModel() {
    private var db = FirebaseFirestore.getInstance()
    var data = ArrayList<FireData>()
    val Livedata = MutableLiveData<ArrayList<FireData>>()
    val user = FirebaseAuth.getInstance().currentUser

    fun getData() {
        //지금 여기 데이터는 한번만 읽는 데이터임 RealTime 으로 불러들이고 싶음
        //.addSnapshotListener 을 사용하면 자동으로 갱신되는 데이터를 읽어들임
        //사용자 별로 데이터를 저장하고 싶음 -> collection 이름을 사용자 uid  로 변경하면됨.ㅍ

        //널체크
        if (user != null) {
            db.collection("community")
                //데이터는 value  로 들어옴
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    data.clear()
                    for (document in value!!) {
                        val firedata = FireData(
                            title = document.data["title"].toString(),
                            description = document.data["description"].toString(),
                            imagename = document.data["imagename"].toString(),
                            id = document.id
                        )
                        data.add(firedata)
                        Livedata.value = data   //라이브 데이터는 add 가 아니고 value 를 바꿔준는 것
                    }
                }
        }


    }

    //날짜 받아오기용 !!
    fun getDataCalender() {
        db.collection(user!!.uid)
            .document("Serve")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                val firedata = FireData(
                    totalserve = value!!["ToTal"].toString(),
                    hasserve = value!!["HasServe"].toString(),
                    leftserve = value!!["LeftServe"].toString(),
                    progressbar = value!!["ProgressBar"].toString()
                )
                data.add(firedata)
                Livedata.value = data

            }
    }

    fun addData(item: FireData) {
        //data.add(item)
        //Livedata.value = data
        //여기서 제목 글을 올려주고
        val hashdata = hashMapOf(
            "title" to item.title,
            "description" to item.description,
            "imagename" to item.imagename

        )
        db.collection("community")
            .add(hashdata)
    }

    //날짜 계산용 !!!!
    fun addDataCalender(item: FireData) {
        val hashdata = hashMapOf(
            "ToTal" to item.totalserve,
            "HasServe" to item.hasserve,
            "LeftServe" to item.leftserve,
            "NickName" to item.nickname,
            "ProgressBar" to item.progressbar,
            "Myname" to item.myname

        )
        db.collection(user!!.uid)
            .document("Serve")
            .set(hashdata)
    }

    //날짜계산 초기화용 !!
    fun addDataCalenderInit() {
        val hashdata = hashMapOf(
            "ToTal" to "0",
            "HasServe" to "0",
            "LeftServe" to "0",
            "NickName" to "0",
            "ProgressBar" to "0",
            "Myname" to "아무개"
        )
        db.collection(user!!.uid)
            .document("Serve")
            .set(hashdata)
    }

    //delete는 내가 지울 문서의 ID 를 알고 있어야함. 지금 코드를 보면 Todo 객체에문서 ID를 가지고 있지 않음.
//그래서 사실은  FIre BAse 데이터를 다룰때애는 document 자체를 가지고 있는게 좋음
//documnet.id 에 아이디가 있음.
    fun deleteData(item: FireData) {
        db.collection(user!!.uid)
            .document(item.id!!)
            .delete()
    }

}