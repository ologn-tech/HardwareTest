package tech.ologn.hardwaretest.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.ologn.hardwaretest.ActivityTests
import tech.ologn.hardwaretest.R
import tech.ologn.hardwaretest.views.WaveformView

class MicrophoneFragment : Fragment() {

    private val handler = android.os.Handler()
    private lateinit var visualizerRunnable: Runnable
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var outputFile: String? = null
    private var isRecording = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_microphone, container, false)

        val headItem = root.findViewById<View>(R.id.headItem)
        val testIcon: ImageView = headItem.findViewById(R.id.test_icon)
        val iconName: TextView = headItem.findViewById(R.id.iconName)
        val btnClick: Button = headItem.findViewById(R.id.btnClick)
        val txtDescription: TextView = headItem.findViewById(R.id.txtDescription)
        val btnSkip: Button = headItem.findViewById(R.id.btnSkip)

        val btnPlay: Button = root.findViewById(R.id.btnPlay)
        val btnConfirm: Button = root.findViewById(R.id.btnConfirm)
        val resultLayout: View = root.findViewById(R.id.resultLayout)

        testIcon.setImageResource(R.drawable.ic_microphone)
        iconName.text = "Microphone"
        btnClick.text = "Record"
        txtDescription.text = "Please click record to check"

        outputFile = "${requireContext().externalCacheDir?.absolutePath}/mic_test.3gp"

        btnClick.setOnClickListener {
            if (checkPermission()) {
                if (!isRecording) {
                    txtDescription.visibility = View.GONE
                    resultLayout.visibility = View.GONE
                    startRecording()
                    btnClick.text = "Stop Recording"
                } else {
                    stopRecording()
                    resultLayout.visibility = View.VISIBLE
                    btnClick.text = "Record"
                }
            } else {
                requestPermission()
            }
        }

        btnPlay.setOnClickListener {
            playRecording()
        }

        btnConfirm.setOnClickListener {
            (requireActivity() as ActivityTests).alertDialogConfirm(
                "Microphone works well",
                "Microphone doesn't work well",
                requireActivity(),
                SoundFragment(),
                { },
                "Did you hear your recorded voice clearly?",
                featureId = TestFragment.ID_MICROPHONE
            )
        }

        btnSkip.setOnClickListener {
            (requireActivity() as ActivityTests).swipeFragment(SoundFragment())
        }

        return root
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(outputFile)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                isRecording = true
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Recording failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        val waveform = view?.findViewById<WaveformView>(R.id.waveformView)
        waveform?.reset()

        visualizerRunnable = object : Runnable {
            override fun run() {
                recorder?.let {
                    val amp = it.maxAmplitude
                    waveform?.updateAmplitude(amp)
                }
                handler.postDelayed(this, 50)
            }
        }
        handler.post(visualizerRunnable)
    }

    private fun stopRecording() {
        try {
            handler.removeCallbacks(visualizerRunnable)

            recorder?.apply {
                stop()
                release()
            }
            isRecording = false
            Toast.makeText(requireContext(), "Recording saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Stop failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        recorder = null
    }

    private fun playRecording() {
        player = MediaPlayer().apply {
            try {
                setDataSource(outputFile)
                prepare()
                start()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Playback failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        val permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.RECORD_AUDIO),
            200
        )
    }

    override fun onDestroy() {
        recorder?.release()
        player?.release()
        super.onDestroy()
    }
}
