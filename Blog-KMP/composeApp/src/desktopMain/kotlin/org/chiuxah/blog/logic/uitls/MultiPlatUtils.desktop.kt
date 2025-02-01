package org.chiuxah.blog.logic.uitls

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO
import org.chiuxah.blog.logic.bean.PlatformType
import java.awt.Desktop
import java.net.URI
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

actual object MultiPlatUtils {
    actual fun createEngine() : HttpClientEngineFactory<*> {
        return CIO
    }

    actual fun getPlatformType(): PlatformType {
        return PlatformType.DESKTOP
    }

    actual fun showMsg(msg : String) {
        SwingUtilities.invokeLater { // 确保 UI 操作在主线程执行
            JOptionPane.showMessageDialog(null, msg)
        }
    }

    actual fun startUrl(url: String) {
        if (Desktop.isDesktopSupported()) {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(URI(url))
            }
        }
    }
}