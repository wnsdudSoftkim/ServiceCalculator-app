package com.junyoung.day

import android.content.Context
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_menu_fragment_tab.*
import kotlinx.android.synthetic.main.menudata.*
import kotlinx.android.synthetic.main.menudata.view.*
import kotlinx.android.synthetic.main.menudata.view.btn_delete

class MenuFragmentTab : Fragment() {


    private val viewModel: DayViewModel by viewModels()
    private lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getData()
        val rootView = inflater.inflate(R.layout.activity_menu_fragment_tab, container, false)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menu_recycler.adapter = RecycleAdapter(viewModel.data,
            LayoutInflater.from(activity),
            onClickDelete = {
                viewModel.deleteData(it)
            }, getShowPost = {
                //position을 showpost 에 전달해줘야함.
                // itemList[position].id  이걸 전달?? 아니면 position 전달?
                // title,description , image 전달?


                val intent = Intent(activity,ShowPost::class.java)
                intent.putExtra("title",it.title)
                intent.putExtra("description",it.description)
                intent.putExtra("imagename",it.imagename)
                //Toast.makeText(activity,""+it.title,Toast.LENGTH_LONG).show()
                startActivity(intent)

            })

        menu_recycler.layoutManager = LinearLayoutManager(activity)
        menu_recycler.adapter?.notifyDataSetChanged()

        //관찰 UI 업데이트
        viewModel.Livedata.observe(viewLifecycleOwner, Observer {
            (menu_recycler.adapter as RecycleAdapter).setData(it)
        })


        btn_add.setOnClickListener {
            startActivity(
                Intent(
                    activity, AddPost::class.java
                )
            )
        }


    }


}

class RecycleAdapter(
    var itemList: ArrayList<FireData>,
    val inflater: LayoutInflater,
    //밖에 firedata 객체를 전달 하고 반환값은 없다.
    val onClickDelete: (firedata: FireData) -> Unit,
    val getShowPost: (firedata: FireData) -> Unit
) : RecyclerView.Adapter<RecycleAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView

        val delete: ImageView

        init {
            title = itemView.findViewById(R.id.data_title)

            delete = itemView.findViewById(R.id.btn_delete)
        }
    }

    //데이터 변화가 있을 시 여기에 넣어줌.
    fun setData(newData: ArrayList<FireData>) {
        itemList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.menudata, parent, false))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.title.setText(itemList[position].title)

        holder.title.setOnClickListener {
            getShowPost.invoke(item)
        }
        holder.delete.setOnClickListener {
            onClickDelete.invoke(item)
        }


    }
}
