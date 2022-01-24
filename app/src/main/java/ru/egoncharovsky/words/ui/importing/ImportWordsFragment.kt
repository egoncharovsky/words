package ru.egoncharovsky.words.ui.importing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import mu.KotlinLogging
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentImportWordsBinding
import ru.egoncharovsky.words.ui.observe
import ru.egoncharovsky.words.ui.openRaw


@DelicateCoroutinesApi
@AndroidEntryPoint
open class ImportWordsFragment : Fragment() {

    private val logger = KotlinLogging.logger { }

    private lateinit var binding: FragmentImportWordsBinding
    private val viewModel: ImportWordsViewModel by viewModels()

    private val importFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            context?.contentResolver?.openInputStream(uri)
                ?.let { viewModel.importCsv(it) }
            Snackbar.make(
                requireParentFragment().requireView(),
                "Import started",
                Snackbar.LENGTH_SHORT
            ).show()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImportWordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.supportedFormats.text = String.format(getString(R.string.supported_formats), "CSV")

        observe(viewModel.isImported()) {
            if (it) {
                Snackbar.make(
                    requireParentFragment().requireView(),
                    getString(R.string.successfully_imported),
                    Snackbar.LENGTH_LONG
                )
                    .show()

                openRaw("words_statistic")?.let(viewModel::importWordPopularityStatistic) ?: run {
                    logger.warn("Words statistic dictionary is not found. Can't use popularity statistic.")
                }
            }
        }
        observe(viewModel.isCleared()) {
            if (it) {
                Snackbar.make(
                    requireParentFragment().requireView(),
                    "Database cleared",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
        observe(viewModel.isPopularityUpgraded()) {
            if (it) {
                Snackbar.make(
                    requireParentFragment().requireView(),
                    getString(R.string.words_popularity_updated),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.importing.setOnClickListener {
            importFile.launch(arrayOf("text/csv", "text/comma-separated-values"))
        }
        binding.clearDatabase.setOnClickListener {
            viewModel.clearDatabase()
        }
    }
}