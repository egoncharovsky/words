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
import ru.egoncharovsky.words.R
import ru.egoncharovsky.words.databinding.FragmentImportWordsBinding
import ru.egoncharovsky.words.ui.observe


@AndroidEntryPoint
open class ImportWordsFragment : Fragment() {

    private lateinit var binding: FragmentImportWordsBinding
    private val importWordsViewModel: ImportWordsViewModel by viewModels()

    private val importFile = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        context?.contentResolver?.openInputStream(uri)?.let { importWordsViewModel.importCsv(it) }
        Snackbar.make(requireParentFragment().requireView(), "Import started", Snackbar.LENGTH_SHORT).show()
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

        observe(importWordsViewModel.isImported()) {
            if (it) {
                Snackbar.make(requireParentFragment().requireView(), "Successfully imported", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
        observe(importWordsViewModel.isCleared()) {
            if (it) {
                Snackbar.make(requireParentFragment().requireView(), "Database cleared", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.importing.setOnClickListener {
            importFile.launch(arrayOf("text/csv", "text/comma-separated-values"))
        }
        binding.clearDatabase.setOnClickListener {
            importWordsViewModel.clearDatabase()
        }
    }
}