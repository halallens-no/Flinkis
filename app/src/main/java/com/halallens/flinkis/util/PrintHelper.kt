package com.halallens.flinkis.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.domain.usecase.PrintableSheet

object PrintHelper {

    data class ThemeColors(
        val primary: String,
        val primaryLight: String,
        val headerBg: String,
        val accent: String
    ) {
        companion object {
            fun fromThemeType(themeType: ThemeType) = when (themeType) {
                ThemeType.BOY -> ThemeColors(
                    primary = "#1976D2",
                    primaryLight = "#63A4FF",
                    headerBg = "#E3F2FD",
                    accent = "#FF6D00"
                )
                ThemeType.GIRL -> ThemeColors(
                    primary = "#AD1457",
                    primaryLight = "#E35183",
                    headerBg = "#FCE4EC",
                    accent = "#FF80AB"
                )
                ThemeType.NEUTRAL -> ThemeColors(
                    primary = "#2E7D32",
                    primaryLight = "#60AD5E",
                    headerBg = "#E8F5E9",
                    accent = "#FFAB00"
                )
            }
        }
    }

    fun printRoutineSheet(context: Context, sheet: PrintableSheet, themeType: ThemeType) {
        val activity = findActivity(context) ?: return
        val colors = ThemeColors.fromThemeType(themeType)
        val htmlContent = generateHtml(sheet, colors)

        val webView = WebView(activity)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val printManager = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = webView.createPrintDocumentAdapter("Flinkis_${sheet.childName}")
                printManager.print(
                    "Flinkis - ${sheet.childName}",
                    printAdapter,
                    PrintAttributes.Builder()
                        .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                        .build()
                )
            }
        }
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun findActivity(context: Context): Activity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    private fun generateHtml(sheet: PrintableSheet, colors: ThemeColors): String {
        val dayHeaders = sheet.dayHeaders.joinToString("") { "<th>$it</th>" }

        val routineRows = sheet.routines.joinToString("") { routine ->
            val cells = sheet.dayHeaders.indices.joinToString("") {
                "<td class='checkbox'></td>"
            }
            "<tr><td class='routine'>${routine.name}</td><td class='slot'>${routine.timeSlot.displayName}</td><td class='pts'>${routine.points}</td>$cells</tr>"
        }

        return """
            <!DOCTYPE html>
            <html>
            <head>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                h1 { font-size: 24px; color: ${colors.primary}; }
                h2 { font-size: 16px; color: #666; margin-bottom: 20px; }
                table { border-collapse: collapse; width: 100%; }
                th, td { border: 1px solid #ddd; padding: 8px; text-align: center; font-size: 12px; }
                th { background-color: ${colors.headerBg}; font-weight: bold; color: ${colors.primary}; }
                .routine { text-align: left; font-weight: 500; min-width: 120px; }
                .slot { font-size: 11px; color: #888; }
                .pts { font-size: 11px; color: ${colors.accent}; font-weight: bold; }
                .checkbox { width: 40px; height: 40px; }
                .footer { margin-top: 20px; font-size: 11px; color: #999; }
                .footer a { color: ${colors.primary}; text-decoration: none; }
            </style>
            </head>
            <body>
                <h1>${sheet.childName}'s Weekly Routine</h1>
                <h2>Week of ${sheet.weekStartDate} - ${sheet.weekEndDate}</h2>
                <table>
                    <tr>
                        <th>Activity</th>
                        <th>Time</th>
                        <th>Pts</th>
                        $dayHeaders
                    </tr>
                    $routineRows
                </table>
                <p class="footer">Printed from Flinkis App by <a href="https://halallens.no">HalalLens</a></p>
            </body>
            </html>
        """.trimIndent()
    }
}
