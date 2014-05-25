package com.mercuryirc.client.ui;

import com.mercuryirc.client.Mercury;
import com.mercuryirc.client.misc.Settings;
import com.mercuryirc.client.ui.misc.FontAwesome;
import com.mercuryirc.model.Server;
import com.mercuryirc.model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ConnectPane extends VBox {
    public static final int DEFAULT_PORT = 6667;

	private final ConnectStage stage;

	private TextField netName = new TextField(Settings.get("default-network-name"));
	private TextField netHost = new TextField(Settings.get("default-network-host"));
	private TextField netPort = new TextField(Settings.get("default-network-port"));
	private TextField netPass = new PasswordField();
	private CheckBox netSsl = new CheckBox();

	private TextField userNick = new TextField(Settings.get("default-user-nickname"));
	private TextField userUser = new TextField(Settings.get("default-user-username"));
	private TextField userReal = new TextField(Settings.get("default-user-real-name"));
	private TextField userPass = new PasswordField();

	public ConnectPane(final ConnectStage stage) {
		this.stage = stage;

		getStyleClass().add("dark-pane");
		setId("container");

		VBox center = new VBox();
		center.setId("center");
		center.getChildren().addAll(
				createHeader("network", true),
				createField("Name", "", netName),
				createField("Host", "", netHost),
				createField("Port", "", netPort),
				createField("Password", "", netPass),
				createField("SSL", "", netSsl),

				createHeader("user", false),
				createField("Nickname", "MercuryUser", userNick),
				createField("Username", "mercury", userUser),
				createField("Real name", "mercury", userReal),
				createField("Password", "", userPass)
		);


		netSsl.setSelected(Boolean.parseBoolean(Settings.get("default-network-ssl")));

		userPass.setText(Settings.get("default-user-password"));

		Button connectButton = new Button("connect");
		connectButton.getStyleClass().add("blue");
		connectButton.setId("connect-button");
		connectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				Server server = new Server(netName.getText(), netHost.getText(), netPort.getText().equals("")
                        ? DEFAULT_PORT : Integer.parseInt(netPort.getText()), netPass.getText().equals("") ?
                        null : netPass.getText(), netSsl.isSelected());

				User user = new User(server, userNick.getText(), userUser.getText(), userReal.getText());
				if (!userPass.getText().equals("")) {
					user.setNickservPassword(userPass.getText());
				}

				//TODO: Actually make a network manager to store multiple networks
				Settings.set("default-network-name", netName.getText());
				Settings.set("default-network-host", netHost.getText());
				Settings.set("default-network-port", netPort.getText());
				Settings.set("default-network-password", netPass.getText());
				Settings.set("default-network-ssl", String.valueOf(netSsl.isSelected()));

				Settings.set("default-user-nickname", userNick.getText());
				Settings.set("default-user-username", userUser.getText());
				Settings.set("default-user-real-name", userReal.getText());
				Settings.set("default-user-password", userPass.getText());



				Mercury.connect(server, user);


				stage.close();
			}
		});
		getChildren().addAll(center, connectButton);
	}

	private Node createHeader(String title, boolean first) {
		Label label = new Label(title);
		label.setId("header");
		if (first) {
			HBox box = new HBox();
			HBox.setHgrow(box, Priority.ALWAYS);
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			Button closeButton = FontAwesome.createIconButton(FontAwesome.REMOVE, "", false, null);
			closeButton.setId("close-button");
			closeButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					stage.close();
				}
			});
			box.getChildren().addAll(label, spacer, closeButton);
			return box;
		}
		return label;
	}

	private HBox createField(String label, String prompt, Control control) {
		if (control instanceof TextField) {
			((TextField) control).setPromptText(prompt);
		}

		HBox box = new HBox();
		Label labelObj = new Label(label);
		labelObj.setMinWidth(100);
		labelObj.setMaxWidth(100);
		control.setMinWidth(100);
		control.setMaxWidth(100);
		box.getChildren().addAll(labelObj, control);
		return box;
	}

}