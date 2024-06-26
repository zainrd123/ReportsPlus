package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.server.ClientUtils.isConnected;

public class settingsController {
	
	//<editor-fold desc="FXML">
	
	
	AnchorPane topBar;
	@javafx.fxml.FXML
	private CheckBox startupFullscreenCheckbox;
	@javafx.fxml.FXML
	private ComboBox ReportWindowComboBox;
	@javafx.fxml.FXML
	private ComboBox mainWindowComboBox;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ComboBox notesWindowComboBox;
	@javafx.fxml.FXML
	private ComboBox reportStyleComboBox;
	@javafx.fxml.FXML
	private ComboBox themeComboBox;
	@javafx.fxml.FXML
	private ColorPicker primPicker;
	@javafx.fxml.FXML
	private ColorPicker secPicker;
	@javafx.fxml.FXML
	private ColorPicker accPicker;
	@javafx.fxml.FXML
	private Label secLabel;
	@javafx.fxml.FXML
	private Label primLabel;
	@javafx.fxml.FXML
	private Label accLabel;
	@javafx.fxml.FXML
	private Button resetDefaultsBtn;
	private actionController controllerVar;
	@javafx.fxml.FXML
	private Button debugLogBtn;
	@javafx.fxml.FXML
	private Button clrLogsBtn;
	@javafx.fxml.FXML
	private Button clrSaveDataBtn;
	@javafx.fxml.FXML
	private Label lbl0;
	@javafx.fxml.FXML
	private Label lbl1;
	@javafx.fxml.FXML
	private Label lbl4;
	@javafx.fxml.FXML
	private Label lbl2;
	@javafx.fxml.FXML
	private Label lbl3;
	@javafx.fxml.FXML
	private ComboBox calloutDurComboBox;
	@javafx.fxml.FXML
	private Label lbl5;
	@javafx.fxml.FXML
	private CheckBox AOTReport;
	@javafx.fxml.FXML
	private CheckBox AOTSettings;
	@javafx.fxml.FXML
	private CheckBox AOTNotes;
	@javafx.fxml.FXML
	private CheckBox AOTCallout;
	@javafx.fxml.FXML
	private CheckBox AOTID;
	@javafx.fxml.FXML
	private CheckBox AOTMap;
	@javafx.fxml.FXML
	private CheckBox AOTDebug;
	@javafx.fxml.FXML
	private CheckBox AOTClient;
	@javafx.fxml.FXML
	private ColorPicker headingPickerReport;
	@javafx.fxml.FXML
	private ColorPicker secPickerReport;
	@javafx.fxml.FXML
	private ColorPicker backgroundPickerReport;
	@javafx.fxml.FXML
	private Label accentLabelReport;
	@javafx.fxml.FXML
	private Label backgroundLabelReport;
	@javafx.fxml.FXML
	private Label secLabelReport;
	@javafx.fxml.FXML
	private ColorPicker accentPickerReport;
	@javafx.fxml.FXML
	private Label lbl6;
	@javafx.fxml.FXML
	private Label headingLabelReport;
	@javafx.fxml.FXML
	private ComboBox presetComboBoxReport;
	@javafx.fxml.FXML
	private ComboBox idDurComboBox;
	@javafx.fxml.FXML
	private Label lbl7;
	
	
	//</editor-fold>
	
	public void initialize() throws IOException {
		if (DataTerminalHomeApplication.controller != null) {
			controllerVar = DataTerminalHomeApplication.controller;
		} else if (newOfficerController.controller != null) {
			controllerVar = newOfficerController.controller;
		} else {
			log("Settings Controller Var could not be set", LogUtils.Severity.ERROR);
		}
		
		topBar = reportUtil.createSimpleTitleBar("ReportsPlus", true);
		
		root.setTop(topBar);
		
		startupFullscreenCheckbox.setSelected(ConfigReader.configRead("fullscreenOnStartup")
		                                                  .equals("true"));
		AOTNotes.setSelected(ConfigReader.configRead("AOTNotes")
		                                 .equals("true"));
		AOTReport.setSelected(ConfigReader.configRead("AOTReport")
		                                  .equals("true"));
		AOTMap.setSelected(ConfigReader.configRead("AOTMap")
		                               .equals("true"));
		AOTID.setSelected(ConfigReader.configRead("AOTID")
		                              .equals("true"));
		AOTCallout.setSelected(ConfigReader.configRead("AOTCallout")
		                                   .equals("true"));
		AOTSettings.setSelected(ConfigReader.configRead("AOTSettings")
		                                    .equals("true"));
		AOTClient.setSelected(ConfigReader.configRead("AOTClient")
		                                  .equals("true"));
		AOTDebug.setSelected(ConfigReader.configRead("AOTDebug")
		                                 .equals("true"));
		
		loadColors();
		loadTheme();
		
		String[] displayPlacements = {"Default", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "\n", "Full Left", "Full Right"};
		mainWindowComboBox.getItems()
		                  .addAll(displayPlacements);
		notesWindowComboBox.getItems()
		                   .addAll(displayPlacements);
		ReportWindowComboBox.getItems()
		                    .addAll(displayPlacements);
		
		try {
			mainWindowComboBox.setValue(ConfigReader.configRead("mainWindowLayout"));
			notesWindowComboBox.setValue(ConfigReader.configRead("notesWindowLayout"));
			ReportWindowComboBox.setValue(ConfigReader.configRead("reportWindowLayout"));
		} catch (IOException e) {
			logError("Could not set reportwindowboxes from config: ", e);
		}
		
		EventHandler<ActionEvent> comboBoxHandler = event -> {
			ComboBox<String> comboBox = (ComboBox<String>) event.getSource();
			String selectedPlacement = comboBox.getSelectionModel()
			                                   .getSelectedItem();
			
			if (comboBox == mainWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("mainWindowLayout", "FullRight");
				}
			}
			
			if (comboBox == notesWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("notesWindowLayout", "FullRight");
				}
			}
			
			if (comboBox == ReportWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("reportWindowLayout", "FullRight");
				}
			}
		};
		
		mainWindowComboBox.setOnAction(comboBoxHandler);
		notesWindowComboBox.setOnAction(comboBoxHandler);
		ReportWindowComboBox.setOnAction(comboBoxHandler);
		
		primPicker.valueProperty()
		          .addListener(new ChangeListener<Color>() {
			          @Override
			          public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				          Color selectedColor = newValue;
				          updateMain(selectedColor);
				          try {
					          loadTheme();
					          loadColors();
				          } catch (IOException e) {
					          logError("LoadTheme IO Error Code 3 ", e);
				          }
			          }
		          });
		
		secPicker.valueProperty()
		         .addListener(new ChangeListener<Color>() {
			         @Override
			         public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				         Color selectedColor = newValue;
				         updateSecondary(selectedColor);
				         try {
					         loadTheme();
					         loadColors();
				         } catch (IOException e) {
					         logError("LoadTheme IO Error Code 4 ", e);
				         }
			         }
		         });
		
		accPicker.valueProperty()
		         .addListener(new ChangeListener<Color>() {
			         @Override
			         public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				         Color selectedColor = newValue;
				         updateAccent(selectedColor);
				         try {
					         loadTheme();
					         loadColors();
				         } catch (IOException e) {
					         logError("LoadTheme IO Error Code 5 ", e);
				         }
			         }
		         });
		
		backgroundPickerReport.valueProperty()
		                      .addListener(new ChangeListener<Color>() {
			                      @Override
			                      public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				                      Color selectedColor = newValue;
				                      updateReportBackground(selectedColor);
				                      try {
					                      loadTheme();
					                      loadColors();
				                      } catch (IOException e) {
					                      logError("LoadTheme IO Error Code 6 ", e);
				                      }
			                      }
		                      });
		
		accentPickerReport.valueProperty()
		                  .addListener(new ChangeListener<Color>() {
			                  @Override
			                  public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				                  Color selectedColor = newValue;
				                  updateReportAccent(selectedColor);
				                  try {
					                  loadTheme();
					                  loadColors();
				                  } catch (IOException e) {
					                  logError("LoadTheme IO Error Code 7 ", e);
				                  }
			                  }
		                  });
		
		headingPickerReport.valueProperty()
		                   .addListener(new ChangeListener<Color>() {
			                   @Override
			                   public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				                   Color selectedColor = newValue;
				                   updateReportHeading(selectedColor);
				                   try {
					                   loadTheme();
					                   loadColors();
				                   } catch (IOException e) {
					                   logError("LoadTheme IO Error Code 8 ", e);
				                   }
			                   }
		                   });
		
		secPickerReport.valueProperty()
		               .addListener(new ChangeListener<Color>() {
			               @Override
			               public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				               Color selectedColor = newValue;
				               updateReportSecondary(selectedColor);
				               try {
					               loadTheme();
					               loadColors();
				               } catch (IOException e) {
					               logError("LoadTheme IO Error Code 9 ", e);
				               }
			               }
		               });
		
		String[] reportdarklight = {"dark", "light"};
		String[] themes = {"dark", "purple", "blue", "grey", "green"};
		String[] presets = {"dark", "light", "grey", "green", "blue"};
		
		reportStyleComboBox.getItems()
		                   .addAll(reportdarklight);
		
		themeComboBox.getItems()
		             .addAll(themes);
		
		presetComboBoxReport.getItems()
		                    .addAll(presets);
		
		try {
			if (ConfigReader.configRead("reportWindowDarkMode")
			                .equals("true")) {
				reportStyleComboBox.getSelectionModel()
				                   .selectFirst();
			} else {
				reportStyleComboBox.getSelectionModel()
				                   .selectLast();
			}
		} catch (IOException e) {
			logError("DarkMode IO Error Code 1 ", e);
			
		}
		
		themeComboBox.setOnAction(actionEvent -> {
			String selectedTheme = (String) themeComboBox.getSelectionModel()
			                                             .getSelectedItem();
			
			switch (selectedTheme) {
				case "dark" -> {
					log("Dark Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#263238"));
					updateSecondary(Color.valueOf("#323C41"));
					updateAccent(Color.valueOf("#505d62"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "purple" -> {
					log("Purple Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#524992"));
					updateSecondary(Color.valueOf("#665cb6"));
					updateAccent(Color.valueOf("#9c95d0"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "blue" -> {
					log("Blue Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d66cc"));
					updateSecondary(Color.valueOf("#6680e6"));
					updateAccent(Color.valueOf("#b3ccff"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "grey" -> {
					log("Grey Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#666666"));
					updateSecondary(Color.valueOf("#808080"));
					updateAccent(Color.valueOf("#4d4d4d"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "green" -> {
					log("Green Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d804d"));
					updateSecondary(Color.valueOf("#669966"));
					updateAccent(Color.valueOf("#99cc99"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				
			}
			
		});
		reportStyleComboBox.setOnAction(event -> {
			if (reportStyleComboBox.getSelectionModel()
			                       .getSelectedItem()
			                       .equals("dark")) {
				ConfigWriter.configwrite("reportWindowDarkMode", "true");
			} else {
				ConfigWriter.configwrite("reportWindowDarkMode", "false");
			}
		});
		
		presetComboBoxReport.setOnAction(actionEvent -> {
			String selectedTheme = (String) presetComboBoxReport.getSelectionModel()
			                                                    .getSelectedItem();
			
			switch (selectedTheme) {
				case "dark" -> {
					log("Dark Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#505d62"));
					updateReportSecondary(Color.valueOf("#323c41"));
					updateReportAccent(Color.valueOf("#263238"));
					updateReportHeading(Color.valueOf("white"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "light" -> {
					log("Light Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#e6e6e6"));
					updateReportSecondary(Color.valueOf("#cccccc"));
					updateReportAccent(Color.valueOf("#b3b3b3"));
					updateReportHeading(Color.valueOf("#333333"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "grey" -> {
					log("Grey Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#4d4d4d"));
					updateReportSecondary(Color.valueOf("gray"));
					updateReportAccent(Color.valueOf("#666666"));
					updateReportHeading(Color.valueOf("white"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "green" -> {
					log("Green Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#80b380"));
					updateReportSecondary(Color.valueOf("#669966"));
					updateReportAccent(Color.valueOf("#4d804d"));
					updateReportHeading(Color.valueOf("white"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
				case "blue" -> {
					log("Blue Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#8099ff"));
					updateReportSecondary(Color.valueOf("#6680e6"));
					updateReportAccent(Color.valueOf("#4d66cc"));
					updateReportHeading(Color.valueOf("white"));
					try {
						loadTheme();
						loadColors();
					} catch (IOException e) {
						logError("LoadTheme Error", e);
					}
				}
			}
			
		});
		
		if (reportStyleComboBox.getSelectionModel()
		                       .getSelectedItem()
		                       .equals("dark")) {
			ConfigWriter.configwrite("reportWindowDarkMode", "true");
		} else {
			ConfigWriter.configwrite("reportWindowDarkMode", "false");
		}
		
		String[] calloutDurations = {"infinite", "1", "3", "5", "7", "10", "12"};
		calloutDurComboBox.getItems()
		                  .addAll(calloutDurations);
		calloutDurComboBox.setValue(ConfigReader.configRead("calloutDuration"));
		calloutDurComboBox.setOnAction(actionEvent -> {
			String selectedDur = (String) calloutDurComboBox.getSelectionModel()
			                                                .getSelectedItem();
			ConfigWriter.configwrite("calloutDuration", selectedDur);
		});
		
		String[] idDurations = {"infinite", "1", "3", "5", "7", "10", "12"};
		idDurComboBox.getItems()
		                  .addAll(idDurations);
		idDurComboBox.setValue(ConfigReader.configRead("IDDuration"));
		idDurComboBox.setOnAction(actionEvent -> {
			String selectedDur = (String) idDurComboBox.getSelectionModel()
			                                                .getSelectedItem();
			ConfigWriter.configwrite("IDDuration", selectedDur);
		});
		
		Platform.runLater(() -> {
			Stage stage = (Stage) root.getScene()
			                          .getWindow();
			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
		});
		
		if (isConnected) {
			controllerVar.getServerStatusLabel()
			             .setStyle(
					             "-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		} else {
			controllerVar.getServerStatusLabel()
			             .setStyle(
					             "-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		}
	}
	
	@javafx.fxml.FXML
	public void clearSaveDataBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene()
		                          .getWindow();
		confirmSaveDataClearDialog(stage);
	}
	
	@javafx.fxml.FXML
	public void openDebugLogsBtnClick(ActionEvent actionEvent) throws IOException {
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("output-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		stage.setTitle("Report Manager");
		stage.setScene(newScene);
		stage.show();
		stage.centerOnScreen();
		stage.setAlwaysOnTop(ConfigReader.configRead("AOTDebug")
		                                 .equals("true"));
		
		windowUtils.centerStageOnMainApp(stage);
	}
	
	@javafx.fxml.FXML
	public void clearLogsBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene()
		                          .getWindow();
		confirmLogClearDialog(stage, controllerVar.getReportChart(), controllerVar.getAreaReportChart());
		showLogClearNotification("Log Manager", "Logs have been cleared.", root);
	}
	
	@javafx.fxml.FXML
	public void resetDefaultsBtnPress(ActionEvent actionEvent) {
		updateMain(Color.valueOf("#524992"));
		updateSecondary(Color.valueOf("#665cb6"));
		updateAccent(Color.valueOf("#9c95d0"));
		try {
			loadTheme();
			loadColors();
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 2 ", e);
		}
	}
	
	private void loadTheme() throws IOException {
		changeBarColors(controllerVar.getReportChart());
		changeStatisticColors(controllerVar.getAreaReportChart());
		
		String mainclr = ConfigReader.configRead("mainColor");
		controllerVar.topPane.setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
		controllerVar.mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.getLogManagerLabelBkg()
		             .setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.getDetailsLabelFill()
		             .setStyle("-fx-text-fill: " + mainclr + ";");
		lbl0.setStyle("-fx-background-color: " + mainclr + ";");
		
		String secclr = ConfigReader.configRead("secondaryColor");
		controllerVar.getServerStatusLabel()
		             .setStyle("-fx-border-color: " + secclr + "; -fx-label-padding: 5; -fx-border-radius: 5;");
		controllerVar.sidepane.setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor3Bkg()
		             .setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor4Bkg()
		             .setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor5Bkg()
		             .setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getReportPlusLabelFill()
		             .setStyle("-fx-text-fill: " + secclr + ";");
		lbl1.setStyle("-fx-text-fill: " + secclr + ";");
		lbl2.setStyle("-fx-text-fill: " + secclr + ";");
		lbl3.setStyle("-fx-text-fill: " + secclr + ";");
		lbl4.setStyle("-fx-text-fill: " + secclr + ";");
		lbl5.setStyle("-fx-text-fill: " + secclr + ";");
		lbl6.setStyle("-fx-text-fill: " + secclr + ";");
		lbl7.setStyle("-fx-text-fill: " + secclr + ";");
		
		String accclr = ConfigReader.configRead("accentColor");
		
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
		String initialStyle = "-fx-background-color: transparent;";
		String nonTransparentBtn = "-fx-background-color: " + accclr + ";";
		controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
		controllerVar.getShowManagerToggle()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn1()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn2()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn3()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn4()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn5()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn6()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn7()
		             .setStyle(nonTransparentBtn);
		controllerVar.getBtn8()
		             .setStyle(nonTransparentBtn);
		
		controllerVar.getBtn1()
		             .setOnMouseEntered(e -> controllerVar.getBtn1()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn1()
		             .setOnMouseExited(e -> controllerVar.getBtn1()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn2()
		             .setOnMouseEntered(e -> controllerVar.getBtn2()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn2()
		             .setOnMouseExited(e -> controllerVar.getBtn2()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn3()
		             .setOnMouseEntered(e -> controllerVar.getBtn3()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn3()
		             .setOnMouseExited(e -> controllerVar.getBtn3()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn4()
		             .setOnMouseEntered(e -> controllerVar.getBtn4()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn4()
		             .setOnMouseExited(e -> controllerVar.getBtn4()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn5()
		             .setOnMouseEntered(e -> controllerVar.getBtn5()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn5()
		             .setOnMouseExited(e -> controllerVar.getBtn5()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn6()
		             .setOnMouseEntered(e -> controllerVar.getBtn6()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn6()
		             .setOnMouseExited(e -> controllerVar.getBtn6()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn7()
		             .setOnMouseEntered(e -> controllerVar.getBtn7()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn7()
		             .setOnMouseExited(e -> controllerVar.getBtn7()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getBtn8()
		             .setOnMouseEntered(e -> controllerVar.getBtn8()
		                                                  .setStyle(hoverStyle));
		controllerVar.getBtn8()
		             .setOnMouseExited(e -> controllerVar.getBtn8()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.getShowManagerToggle()
		             .setOnMouseEntered(e -> controllerVar.getShowManagerToggle()
		                                                  .setStyle(hoverStyle));
		controllerVar.getShowManagerToggle()
		             .setOnMouseExited(e -> controllerVar.getShowManagerToggle()
		                                                 .setStyle(nonTransparentBtn));
		controllerVar.shiftInfoBtn.setOnMouseEntered(e -> controllerVar.shiftInfoBtn.setStyle(hoverStyle));
		controllerVar.shiftInfoBtn.setOnMouseExited(e -> controllerVar.shiftInfoBtn.setStyle(initialStyle));
		controllerVar.getSettingsBtn()
		             .setOnMouseEntered(e -> controllerVar.getSettingsBtn()
		                                                  .setStyle("-fx-background-color: " + secclr + ";"));
		controllerVar.getSettingsBtn()
		             .setOnMouseExited(e -> controllerVar.getSettingsBtn()
		                                                 .setStyle(initialStyle));
		controllerVar.notesButton.setOnMouseEntered(e -> controllerVar.notesButton.setStyle(hoverStyle));
		controllerVar.notesButton.setOnMouseExited(e -> controllerVar.notesButton.setStyle(initialStyle));
		controllerVar.getCreateReportBtn()
		             .setOnMouseEntered(e -> controllerVar.getCreateReportBtn()
		                                                  .setStyle(hoverStyle));
		controllerVar.getCreateReportBtn()
		             .setOnMouseExited(e -> controllerVar.getCreateReportBtn()
		                                                 .setStyle(initialStyle));
		controllerVar.getLogsButton()
		             .setOnMouseEntered(e -> controllerVar.getLogsButton()
		                                                  .setStyle(hoverStyle));
		controllerVar.getLogsButton()
		             .setOnMouseExited(e -> controllerVar.getLogsButton()
		                                                 .setStyle(initialStyle));
		controllerVar.getMapButton()
		             .setOnMouseEntered(e -> controllerVar.getMapButton()
		                                                  .setStyle(hoverStyle));
		controllerVar.getMapButton()
		             .setOnMouseExited(e -> controllerVar.getMapButton()
		                                                 .setStyle(initialStyle));
		controllerVar.getShowIDBtn()
		             .setOnMouseEntered(e -> controllerVar.getShowIDBtn()
		                                                  .setStyle(hoverStyle));
		controllerVar.getShowIDBtn()
		             .setOnMouseExited(e -> controllerVar.getShowIDBtn()
		                                                 .setStyle(initialStyle));
		controllerVar.getShowCalloutBtn()
		             .setOnMouseEntered(e -> controllerVar.getShowCalloutBtn()
		                                                  .setStyle(hoverStyle));
		controllerVar.getShowCalloutBtn()
		             .setOnMouseExited(e -> controllerVar.getShowCalloutBtn()
		                                                 .setStyle(initialStyle));
		controllerVar.getLookupBtn()
		             .setOnMouseEntered(e -> controllerVar.getLookupBtn()
		                                                  .setStyle(hoverStyle));
		controllerVar.getLookupBtn()
		             .setOnMouseExited(e -> controllerVar.getLookupBtn()
		                                                 .setStyle(initialStyle));
		
		controllerVar.updateInfoBtn.setOnMouseEntered(e -> controllerVar.updateInfoBtn.setStyle(hoverStyle));
		controllerVar.updateInfoBtn.setOnMouseExited(e -> {
			controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
		});
		
		if (isConnected) {
			controllerVar.getServerStatusLabel()
			             .setStyle(
					             "-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		} else {
			controllerVar.getServerStatusLabel()
			             .setStyle(
					             "-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		}
	}
	
	private void loadColors() {
		try {
			Color primary = Color.valueOf(ConfigReader.configRead("mainColor"));
			Color secondary = Color.valueOf(ConfigReader.configRead("secondaryColor"));
			Color accent = Color.valueOf(ConfigReader.configRead("accentColor"));
			
			Color reportBackground = Color.valueOf(ConfigReader.configRead("reportBackground"));
			Color reportSecondary = Color.valueOf(ConfigReader.configRead("reportSecondary"));
			Color reportAccent = Color.valueOf(ConfigReader.configRead("reportAccent"));
			Color reportHeading = Color.valueOf(ConfigReader.configRead("reportHeading"));
			
			primPicker.setValue(primary);
			secPicker.setValue(secondary);
			accPicker.setValue(accent);
			primLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			secLabel.setStyle("-fx-text-fill: " + toHexString(secondary) + ";");
			accLabel.setStyle("-fx-text-fill: " + toHexString(accent) + ";");
			
			backgroundPickerReport.setValue(reportBackground);
			accentPickerReport.setValue(reportAccent);
			headingPickerReport.setValue(reportHeading);
			secPickerReport.setValue(reportSecondary);
			backgroundLabelReport.setStyle("-fx-text-fill: " + toHexString(reportBackground) + ";");
			accentLabelReport.setStyle("-fx-text-fill: " + toHexString(reportAccent) + ";");
			secLabelReport.setStyle("-fx-text-fill: " + toHexString(reportSecondary) + ";");
			
			try {
				String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
				String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("accentColor") + ";";
				resetDefaultsBtn.setStyle(nonTransparentBtn);
				resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
				resetDefaultsBtn.setOnMouseExited(e -> resetDefaultsBtn.setStyle(nonTransparentBtn));
				clrLogsBtn.setStyle(nonTransparentBtn);
				clrLogsBtn.setOnMouseEntered(e -> clrLogsBtn.setStyle(hoverStyle));
				clrLogsBtn.setOnMouseExited(e -> clrLogsBtn.setStyle(nonTransparentBtn));
				clrSaveDataBtn.setStyle(nonTransparentBtn);
				clrSaveDataBtn.setOnMouseEntered(e -> clrSaveDataBtn.setStyle(hoverStyle));
				clrSaveDataBtn.setOnMouseExited(e -> clrSaveDataBtn.setStyle(nonTransparentBtn));
				debugLogBtn.setStyle(nonTransparentBtn);
				debugLogBtn.setOnMouseEntered(e -> debugLogBtn.setStyle(hoverStyle));
				debugLogBtn.setOnMouseExited(e -> debugLogBtn.setStyle(nonTransparentBtn));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 1 ", e);
		}
	}
	
	@javafx.fxml.FXML
	public void startupFullscreenClick(ActionEvent actionEvent) {
		if (startupFullscreenCheckbox.isSelected()) {
			ConfigWriter.configwrite("fullscreenOnStartup", "true");
			startupFullscreenCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("fullscreenOnStartup", "false");
			startupFullscreenCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void settingsAOTClick(ActionEvent actionEvent) {
		if (AOTSettings.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "true");
			AOTSettings.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "false");
			AOTSettings.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void reportAOTClick(ActionEvent actionEvent) {
		if (AOTReport.isSelected()) {
			ConfigWriter.configwrite("AOTReport", "true");
			AOTReport.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTReport", "false");
			AOTReport.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void mapAOTClick(ActionEvent actionEvent) {
		if (AOTMap.isSelected()) {
			ConfigWriter.configwrite("AOTMap", "true");
			AOTMap.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTMap", "false");
			AOTMap.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void calloutAOTClick(ActionEvent actionEvent) {
		if (AOTCallout.isSelected()) {
			ConfigWriter.configwrite("AOTCallout", "true");
			AOTCallout.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTCallout", "false");
			AOTCallout.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void IDAOTClick(ActionEvent actionEvent) {
		if (AOTID.isSelected()) {
			ConfigWriter.configwrite("AOTID", "true");
			AOTID.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTID", "false");
			AOTID.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void NotesAOTClick(ActionEvent actionEvent) {
		if (AOTNotes.isSelected()) {
			ConfigWriter.configwrite("AOTNotes", "true");
			AOTNotes.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTNotes", "false");
			AOTNotes.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void ClientAOTClick(ActionEvent actionEvent) {
		if (AOTClient.isSelected()) {
			ConfigWriter.configwrite("AOTClient", "true");
			AOTClient.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTClient", "false");
			AOTClient.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void debugAOTClick(ActionEvent actionEvent) {
		if (AOTDebug.isSelected()) {
			ConfigWriter.configwrite("AOTDebug", "true");
			AOTDebug.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTDebug", "false");
			AOTDebug.setSelected(false);
		}
	}
	
	
}
