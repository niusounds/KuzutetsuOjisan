package com.niusounds.pyon

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var uxFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uxFragment = ux_fragment as ArFragment

        uxFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            ViewRenderable.builder()
                    .setView(this, R.layout.pyon)
                    .build()
                    .thenAccept { pyonRenderable ->
                        pyonRenderable.isShadowCaster = false
                        val imageView = pyonRenderable.view.findViewById<ImageView>(R.id.image)
                        val animationDrawable = imageView.drawable as AnimationDrawable
                        animationDrawable.start()

                        val anchorNode = AnchorNode(hitResult.createAnchor()).apply {
                            setParent(uxFragment.arSceneView.scene)
                        }

                        val pyonNode = TransformableNode(uxFragment.transformationSystem).apply {
                            renderable = pyonRenderable
                            setParent(anchorNode)
                        }
                    }
                    .exceptionally { ex ->
                        Toast.makeText(this, ex.localizedMessage, Toast.LENGTH_SHORT).show()
                        return@exceptionally null
                    }
        }
    }
}
