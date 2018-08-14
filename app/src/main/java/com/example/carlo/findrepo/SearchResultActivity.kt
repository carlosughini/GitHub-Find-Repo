package com.example.carlo.findrepo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val searchTerm = intent.getStringExtra("searchTerm")
        println("Search term: $searchTerm")
        title = searchTerm

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val repoRetriever = RepoRetriever()

        if (searchTerm != null) {
            val callback = object : retrofit2.Callback<SearchRepo> {

                override fun onFailure(call: Call<SearchRepo>?, t: Throwable?) {
                    println("It is not working")
                }

                override fun onResponse(call: Call<SearchRepo>?, response: Response<SearchRepo>?) {
                    val searchResult = response?.body()
                    //val repositories = mutableListOf<String>()

                    if (searchResult != null) {
                        /*for (repo in searchResult.items) {
                            repositories.add(repo.full_name)
                            //println("Repo full name: ${repo.full_name}")
                        }*/
                    }
                    val repoList = findViewById<ListView>(R.id.repoList)
                    repoList.setOnItemClickListener { parent, view, position, id ->
                        val selectedRepo = searchResult!!.items[position]
                        val urlIntent = Intent(Intent.ACTION_VIEW)
                        urlIntent.data = Uri.parse(selectedRepo.html_url)
                        startActivity(urlIntent)
                    }

                    val adapter = RepoAdapter(this@SearchResultActivity,android.R.layout.simple_list_item_1,searchResult!!.items)
                    repoList.adapter = adapter
                    progressBar.visibility = View.INVISIBLE
                }
            }
            repoRetriever.getRepo(callback, searchTerm)
        } else {
            val username = intent.getStringExtra("username")
            val callback = object : retrofit2.Callback<List<RepoItems>> {

                override fun onFailure(call: Call<List<RepoItems>>?, t: Throwable?) {
                    println("It is not working")
                }

                override fun onResponse(call: Call<List<RepoItems>>?, response: Response<List<RepoItems>>?) {
                    if (response?.code() == 404) {
                        val mRootView = findViewById<View>(R.id.searchView)
                        Snackbar.make(mRootView,"User not found! :(",Snackbar.LENGTH_LONG).show()
                    } else {
                        val repos = response?.body()
                        val repositoriesNames = mutableListOf<String>()

                        if (repos != null) {
                            for (repo in repos) {
                                repositoriesNames.add(repo.name)
                            }
                            val repoList = findViewById<ListView>(R.id.repoList)
                            val adapter = RepoAdapter(this@SearchResultActivity,android.R.layout.simple_list_item_1,repos)
                            repoList.adapter = adapter
                        }
                    }
                    progressBar.visibility = View.INVISIBLE
                }

            }
            repoRetriever.userRepos(callback, username)

        }



    }
}

class RepoAdapter(context: Context?, resource: Int, objects: List<RepoItems>?) : ArrayAdapter<RepoItems>(context, resource, objects) {

    override fun getCount(): Int {
        return super.getCount()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val repoView = inflator.inflate(R.layout.repo_list_row,parent, false)

        val repoName = repoView.findViewById<TextView>(R.id.repoName)
        val repoImage = repoView.findViewById<ImageView>(R.id.repoImage)

        val repo = getItem(position)
        repoName.text = repo.full_name

        Picasso.get()
                .load(repo.owner.avatar_url)
                .resize(50, 50)
                .centerCrop()
                .into(repoImage)

        return repoView
    }

}
