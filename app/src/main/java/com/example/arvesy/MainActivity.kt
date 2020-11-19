package com.example.arvesy

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat.setBackground
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var arrayView : Array<View>

    lateinit var vesyRenderable:ModelRenderable

    internal var selected = 1

    lateinit var arFragment:ArFragment

    override fun onClick(view: View?) {
        if(view!!.id == R.id.vesy)
        {
            selected = 1
            mySetBackground(view!!.id)
        }
    }

    private fun mySetBackground(id: Int) {
        for(i in arrayView.indices)
        {
            if(arrayView[i].id == id)
                arrayView[i].setBackgroundColor(Color.parseColor("#80333639"))
            else
                arrayView[i].setBackgroundColor(Color.TRANSPARENT)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupArray()
        setupClickListener()
        setupModel()

        arFragment = supportFragmentManager.findFragmentById(R.id.arvesy) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment.arSceneView.scene)

            createModel(anchorNode, selected)
        }
    }

    private fun createModel(anchorNode: AnchorNode, selected: Int) {
        if(selected == 1)
        {
            val vesy = TransformableNode(arFragment.transformationSystem)
            vesy.setParent(anchorNode)
            vesy.renderable = vesyRenderable
            vesy.select()
        }

    }

    private fun setupModel() {

        ModelRenderable.builder()
            .setSource(this, R.raw.vesy_bez_animatsii)
            .build()
            .thenAccept { t: ModelRenderable? -> vesyRenderable  }
            .exceptionally { t: Throwable? ->
                Toast.makeText(this@MainActivity,"Unnable to load Vesy model", Toast.LENGTH_SHORT).show()
                null
              }

    }

    private fun setupClickListener() {
        for(i in arrayView.indices)
        {
            arrayView[i].setOnClickListener(this)
        }
    }

    private fun setupArray() {
        arrayView = arrayOf(
            vesy
        )
    }
}
