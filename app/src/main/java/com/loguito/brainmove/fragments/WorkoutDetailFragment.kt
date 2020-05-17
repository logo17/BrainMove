package com.loguito.brainmove.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.loguito.brainmove.R
import com.loguito.brainmove.adapters.WorkoutDetailAdapter
import com.loguito.brainmove.models.remote.Workout
import kotlinx.android.synthetic.main.fragment_workout_detail.*


class WorkoutDetailFragment : Fragment() {
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initViews()
    }

    private fun initRecyclerView() {
        val adapter = WorkoutDetailAdapter()
        adapter.workoutClicked.observe(viewLifecycleOwner, Observer {
            overlayWorkoutImageView.gifUrl = it.videoUrl
        })
        adapter.workoutList = mockListData()
        workoutRecyclerView.adapter = adapter
    }

    private fun initViews() {
        // TODO: Remove this
        Glide.with(this).load("https://www.dropbox.com/s/a92h9zq4fd5u8lm/brain_main.jpg?dl=1").into(imageView2)
        workoutTitleTextView.text = "Tabata abdominales"
        workoutDescriptionTextView.text =
            "Fortalece los musculos abdominales al ritmo de las mejores canciones."
        dateView.detailText = "15/05/2020"
        trainerView.detailText = "Dulce Urena Madrigal"
    }

    // TODO: Remove this
    private fun mockListData(): List<Workout> {
        val result = ArrayList<Workout>()
        result.add(
            Workout(
                "Caída frontal",
                "https://www.hola.com/imagenes/estar-bien/20190122136083/riesgos-hacer-abdominales-a-diario/0-638-663/abdominales-diarios-t.jpg?filter=w600&filter=ds75",
                "https://media.giphy.com/media/l0ErQUmv2LKD4vZ2E/giphy.gif"
            )
        )
        result.add(
            Workout(
                "Tijeras",
                "https://www.hola.com/imagenes/estar-bien/20190122136083/riesgos-hacer-abdominales-a-diario/0-638-663/abdominales-diarios-t.jpg?filter=w600&filter=ds75",
                "https://media.giphy.com/media/iQK5zCZL4mBuU/giphy.gif"
            )
        )
        result.add(
            Workout(
                "Toco rodillas",
                "https://www.hola.com/imagenes/estar-bien/20190122136083/riesgos-hacer-abdominales-a-diario/0-638-663/abdominales-diarios-t.jpg?filter=w600&filter=ds75",
                "https://media.giphy.com/media/iQK5zCZL4mBuU/giphy.gif"
            )
        )
        result.add(
            Workout(
                "Toco puntas",
                "https://www.hola.com/imagenes/estar-bien/20190122136083/riesgos-hacer-abdominales-a-diario/0-638-663/abdominales-diarios-t.jpg?filter=w600&filter=ds75",
                "https://media.giphy.com/media/iQK5zCZL4mBuU/giphy.gif"
            )
        )
        return result
    }
}