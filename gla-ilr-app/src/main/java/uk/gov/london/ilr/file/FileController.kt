/**
 * Copyright (c) Greater London Authority, 2016.
 *
 * This source code is licensed under the Open Government Licence 3.0.
 *
 * http://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/
 */
package uk.gov.london.ilr.file

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import uk.gov.london.ilr.file.ESFMonthlyRecordFile.validateESFMonthlyRecordFile
import uk.gov.london.ilr.security.UserService

@Controller
class FileController(private val fileUploadHandler: FileUploadHandler,
                     private val dataImportService: DataImportService,
                     private val userService: UserService) {

    @PreAuthorize("hasAnyRole('OPS_ADMIN', 'GLA_ORG_ADMIN', 'GLA_SPM', 'GLA_PM', 'GLA_FINANCE', 'GLA_READ_ONLY')")
    @PostMapping("/upload")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        try {
            val fileName = file.originalFilename
            val importType = getDataImportTypeFromFileName(fileName)
            if (importType.isMonthlyFile) {
                validateESFMonthlyRecordFile(fileName, importType)
            }

            val result: UploadResult
            if (file.size > 1000000) {
                result = fileUploadHandler.uploadAsync(fileName, file.inputStream, importType, userService.currentUserName())
            }
            else {
                result = fileUploadHandler.upload(fileName, file.inputStream, importType, userService.currentUserName())
            }

            if (result.errorMessages.isNotEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessageList", result.errorMessages)
            }
        }
        catch (e: Exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload due to: ${e.message}")
        }

        return "redirect:/files"
    }

    fun getDataImportTypeFromFileName(fileName: String?) = (DataImportType.getTypeByFilename(fileName!!)
            ?: throw IllegalArgumentException("Unable to identify file type by filename: $fileName"))

    @PreAuthorize("hasAnyRole('OPS_ADMIN', 'GLA_ORG_ADMIN', 'GLA_SPM', 'GLA_PM', 'GLA_FINANCE', 'GLA_READ_ONLY')")
    @GetMapping("/files")
    fun filesPage(model: Model): String {
        model["dataImports"] = dataImportService.dataImports()
        return "files"
    }

    @PreAuthorize("hasAnyRole('OPS_ADMIN', 'GLA_ORG_ADMIN', 'GLA_SPM', 'GLA_PM', 'GLA_FINANCE')")
    @PostMapping("/deleteFile")
    fun deleteFile(@RequestParam("id") id: Int, redirectAttributes: RedirectAttributes): String {
        dataImportService.delete(id)
        redirectAttributes.addFlashAttribute("infoMessage", "File successfully deleted")
        return "redirect:/files"
    }

}
