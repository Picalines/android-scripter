package com.picalines.scripter.ui.screens.script_list

import com.picalines.scripter.LOGIN_SCREEN
import com.picalines.scripter.SCRIPT_DEFAULT_ID
import com.picalines.scripter.SCRIPT_ID
import com.picalines.scripter.SCRIPT_LIST_SCREEN
import com.picalines.scripter.domain.service.AccountService
import com.picalines.scripter.domain.service.StorageService
import com.picalines.scripter.ui.screens.ScripterAppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScriptListViewModel @Inject constructor(
    private val accountService: AccountService,
    storageService: StorageService
) : ScripterAppViewModel() {
    val scripts = storageService.scripts

    fun initialize(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.currentUser.collect { user ->
                if (user == null) restartApp(
                    LOGIN_SCREEN
                )
            }
        }
    }

    fun onAddClick(openScreen: (String) -> Unit) {
        openScreen("$SCRIPT_LIST_SCREEN?$SCRIPT_ID=$SCRIPT_DEFAULT_ID")
    }

    fun onEditClick(openScreen: (String) -> Unit, scriptId: String) {
        openScreen("$SCRIPT_LIST_SCREEN?$SCRIPT_ID=$scriptId")
    }
}