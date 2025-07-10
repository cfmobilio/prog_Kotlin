import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wa.R
import com.example.wa.presentation.initialQuiz.QuizViewModel
import com.example.wa.data.model.QuizResult

class ResultFragment : Fragment() {

    private val viewModel: QuizViewModel by viewModels()

    enum class ResultType(
        val layoutId: Int,
        val buttonId: Int,
        val actionId: Int
    ) {
        BASE(
            R.layout.base_result,
            R.id.buttonContinuaBase,
            R.id.action_base_result_to_accessoFragment
        ),
        INTERMEDIATE(
            R.layout.intermediate_result,
            R.id.buttonContinuaIntermedio,
            R.id.action_intermediate_result_to_accessoFragment
        ),
        ADVANCED(
            R.layout.advanced_result,
            R.id.buttonContinuaAvanzato,
            R.id.action_advanced_result_to_accessoFragment
        )
    }

    private var resultType: ResultType = ResultType.BASE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val typeString = arguments?.getString(ARG_RESULT_TYPE) ?: "BASE"
        resultType = ResultType.valueOf(typeString)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(resultType.layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.quizResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                updateUI(view, it)
            }
        }

        view.findViewById<Button>(resultType.buttonId).setOnClickListener {
            findNavController().navigate(resultType.actionId)
        }
    }

    private fun updateUI(view: View, result: QuizResult) {

        when (resultType) {
            ResultType.BASE -> {
                R.id.action_base_result_to_accessoFragment
            }
            ResultType.INTERMEDIATE -> {
                R.id.action_intermediate_result_to_accessoFragment
            }
            ResultType.ADVANCED -> {
                R.id.action_intermediate_result_to_accessoFragment
            }
        }
    }

    companion object {
        private const val ARG_RESULT_TYPE = "result_type"

        fun newInstanceBase(): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RESULT_TYPE, ResultType.BASE.name)
                }
            }
        }

        fun newInstanceIntermediate(): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RESULT_TYPE, ResultType.INTERMEDIATE.name)
                }
            }
        }

        fun newInstanceAdvanced(): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RESULT_TYPE, ResultType.ADVANCED.name)
                }
            }
        }
    }
}