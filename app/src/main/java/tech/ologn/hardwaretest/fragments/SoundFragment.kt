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
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.databinding.FragmentSoundBinding

class SoundFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentSoundBinding.inflate(inflater,container,false)

        binding.sound.testIcon.setImageResource(R.drawable.ic_sound)
        binding.sound.txtDescription.text = "Click on button to check sound"
        binding.sound.iconName.text = "Sound"
        binding.sound.btnClick.text = "Check Sound"
        binding.sound.btnClick.setOnClickListener {
            checkSound()
        }
        binding.sound.btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(AccelerometerFragment())
        }
        return binding.root
    }

    private fun checkSound() {
        val audioManager = requireActivity().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
            0
        )

        fun playChannel(leftVol: Float, rightVol: Float, onComplete: () -> Unit) {
            val mp = MediaPlayer.create(requireActivity(), R.raw.mpsound)
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
                        }
                    }, 300)
                }
            }, 300)
        }
    }

}
