package ru.momentkesh.kickfootball

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.huda.kickfoot.GameOverFragment
import com.huda.kickfoot.MainActivity
import ru.momentkesh.kickfootball.databinding.FragmentGameOverBinding
import ru.momentkesh.kickfootball.databinding.FragmentRecordBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var paused: Boolean = false
    private var points: Int = 0
    private var high: Int = 0
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            paused = it.getBoolean("paused", false)
            points = it.getInt("points", 0)
            high = it.getInt("high", 0)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<ImageView>(R.id.back)
        binding.ok.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)

            startActivity(intent)        }
        binding.exit.setOnClickListener {
            requireActivity().finish()

        }
        button?.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        val textView: TextView = view.findViewById(R.id.textInp)
        textView.text = "$points"

        val textView2: TextView = view.findViewById(R.id.bestScore)
        textView2.text = "$high"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameOverFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameOverFragment().apply {
                arguments = Bundle().apply {

                }
            }
    } }

