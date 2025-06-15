package tech.ologn.hardwaretest.fragments

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R

class SoundFragment : Fragment() {
    private lateinit var btnClick: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_sound, container, false)

        val soundItem = root.findViewById<View>(R.id.sound)
        val testIcon: ImageView = soundItem.findViewById(R.id.test_icon)
        val iconName: TextView = soundItem.findViewById(R.id.iconName)
        val txtDescription: TextView = soundItem.findViewById(R.id.txtDescription)
        btnClick = soundItem.findViewById(R.id.btnClick)
        val btnSkip: Button = soundItem.findViewById(R.id.btnSkip)

        testIcon.setImageResource(R.drawable.ic_sound)
        txtDescription.text = "Click on button to check sound"
        iconName.text = "Sound"
        btnClick.text = "Check Sound"

        btnClick.setOnClickListener {
            checkSound()
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(AccelerometerFragment())
        }

        return root
    }

    private fun checkSound() {
        btnClick.isEnabled = false
        val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        fun playChannel(leftVol: Float, rightVol: Float, onComplete: () -> Unit) {
            val act = activity ?: return
            val mp = MediaPlayer.create(act, R.raw.mpsound)
            mp.setVolume(leftVol, rightVol)
            mp.setOnCompletionListener {
                mp.release()
                onComplete()
            }
            mp.start()
        }

        playChannel(1f, 0f) { // Left
            Handler().postDelayed({
                playChannel(0f, 1f) { // Right
                    Handler().postDelayed({
                        playChannel(1f, 1f) { // Both
                            (requireActivity() as ActivityTests).alertDialogConfirm(
                                "Sound works well",
                                "Sound doesn't work well",
                                requireActivity(),
                                AccelerometerFragment(),
                                { checkSound() },
                                "Did you hear the sound from left, then right, then both speakers?",
                                featureId = TestFragment.ID_SOUND
                            )
                            btnClick.isEnabled = true
                        }
                    }, 300)
                }
            }, 300)
        }
    }

}
