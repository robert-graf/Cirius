package gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public class DropFile {
	public DropFile() {
		DropTarget target = new DropTarget(AAufbau.f, new DropTargetAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
							.getTransferable().getTransferData(
									DataFlavor.javaFileListFlavor);
					for (File file : droppedFiles) {
						// Relativieren
						file = new File(new File("").toURI()
								.relativize(file.toURI()).getPath());
						boolean open = true;
						if (AAufbau.f.CurrendPanel instanceof Drop) {
							open = ((Drop) AAufbau.f.CurrendPanel)
									.dropEvent(file);
						}
						if (open) {
							Öffnen.InitAndOpen(file);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		AAufbau.f.setDropTarget(target);
	}
}
