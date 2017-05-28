package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import ia_math.IAMath;
import ia_math.RealInterval;

public class Main extends Application implements Initializable {
	
	private int st = 0;
	private static int index = -1;
	private int pkt = 0, pkt2 = 0;
	private double odl = 0;
	private double mn, mx;
	private static double p;
	private static int aryt = -1;

	@FXML
	private Button interpoluj;

	@FXML
	private RadioButton z_reki_funkcja;
	
	@FXML
	private RadioButton domyslna;
	
	@FXML
	private RadioButton z_reki_punkty;
	
	@FXML
	private RadioButton rownoodlegle;

	@FXML
	private RadioButton czebyszewa;

	@FXML
	private TextField stopien_wielomianu;
	
	@FXML
	private Button zatwierdz;
	
	@FXML
	private TextField wspolczynnik;
	
	@FXML
	private Button dodaj_wspolczynnik;
	
	@FXML
	private Label funkcja;
	
	@FXML
	private ChoiceBox<String> ktora_domyslna;
	
	@FXML
	private TextField parametr;
	
	@FXML
	private Button zatwierdz2;
	
	@FXML
	private Button reset;
	
	@FXML
	private TextField ilosc_punktow;
	
	@FXML
	private Button zatwierdz3;
	
	@FXML
	private TextField odcieta;
	
	@FXML
	private Button dodaj_punkt;
	
	@FXML
	private TextField odleglosc;
	
	@FXML
	private Button zatwierdz4;
	
	@FXML
	private Button reset2;
	
	@FXML
	private Label punkty;
	
	@FXML
	private TextField min;
	
	@FXML
	private TextField max;
	
	@FXML
	private Button zatwierdz5;
	
	@FXML
	private Button reset3;
	
	@FXML
	private Button zatwierdz6;
	
	@FXML
	private ChoiceBox<String> arytmetyka;

	private ScatterChart scatterChart;
	private NumberAxis chartX;
	private NumberAxis chartY;
	
	private List<Double> funkcja_uzytkownika;
	private List<Double> punkty_uzytkownika;
	
	private GridPane gridPane;
	private Label funkcja_interpolowana;
	private Label funkcja_bazowa;
	private TextField odcieta2;
	private Button oblicz;
	private Label wx;
	private Label fx;
	private Label delta;
	private Label pom1, pom2;
	private double[] y1;
	private RealInterval[] y2;
	
	
	private void wykres() throws IOException {
		//PIERWSZY WYKRES----------------------------------------------------------------------
		chartX = new NumberAxis();
        chartY = new NumberAxis();
        String[] wykres = {"x^", "^x", "*sin(x)", "*|x|"};
        final ScatterChart<Number,Number> scatterChart = new ScatterChart<Number,Number>(chartX,chartY);   
        scatterChart.setAnimated(false);
        XYChart.Series series = new XYChart.Series();
        
        if(index == 0) {
        	series.setName(wykres[index]+p);
        	for(double i=mn; i<=mx; i+=0.01) {
        		series.getData().add(new XYChart.Data(i, Math.pow(i, p)));
        	}
        }
        
        else if(index == 1) {
        	series.setName(p+wykres[index]);
        	for(double i=mn; i<=mx; i+=0.01) {
        		series.getData().add(new XYChart.Data(i, Math.pow(p, i)));
        	}
        }
        
        else if(index == 2) {
        	series.setName(p+wykres[index]);
        	for(double i=mn; i<=mx; i+=0.01) {
        		series.getData().add(new XYChart.Data(i, p*Math.sin(i)));
        	}
        }
        else if(index == 3) {
        	series.setName(p+wykres[index]);
        	for(double i=mn; i<=mx; i+=0.01) {
        		series.getData().add(new XYChart.Data(i, p*Math.abs(i)));
        	}
        }
        
        else {
        	series.setName(funkcja.getText());
        	for(double i=mn; i<=mx; i+=0.001) {
        		double fun = 0;
        		for(int w=0; w<funkcja_uzytkownika.size(); w++) {
        			fun += Math.pow(i, w)*funkcja_uzytkownika.get(w);
        		}
        		series.getData().add(new XYChart.Data(i, fun));
        	}
        }
        
        //DRUGI WYKRES-------------------------------------------------------------------------
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("wielomian interpolacyjny");
      
        if(aryt==0) {
        	double[] x = new double[punkty_uzytkownika.size()];
    		for(int i=0; i<punkty_uzytkownika.size(); i++) {
    			x[i]=punkty_uzytkownika.get(i);System.out.println(x[i]);
    		}
    		
    		double[] y = new double[punkty_uzytkownika.size()];
    		for(int i=0; i<punkty_uzytkownika.size(); i++){
    			if(index == 0) {
    				y[i] = Math.pow(punkty_uzytkownika.get(i), p);
    			}
    			else if(index == 1) {
    				y[i] = Math.pow(p, punkty_uzytkownika.get(i));
    			}
    			else if(index == 2) {
    				y[i] = Math.sin(punkty_uzytkownika.get(i))*p;
    			}
    			else if(index == 3) {
    				y[i] = Math.abs(punkty_uzytkownika.get(i))*p;
    			}
    			else {
    				for(int w=0; w<funkcja_uzytkownika.size(); w++) {
            			y[i] += Math.pow(x[i], w)*funkcja_uzytkownika.get(w);
            		}
    			}
    		}
        	
        	y=interpolacja(x,y);
    		y1=y;
    		
    		gridPane = new GridPane();
    		gridPane.setMinSize(400, 200); 
    	    gridPane.setPadding(new Insets(10, 10, 10, 10)); 
    	    gridPane.setVgap(5); 
    	    gridPane.setHgap(5);       
    	    gridPane.setAlignment(Pos.CENTER); 
    		
    	    funkcja_bazowa = new Label();
    	    funkcja_bazowa.setText("f(x) = "+series.getName());
    	    gridPane.add(funkcja_bazowa, 0, 0);
    	    
    		funkcja_interpolowana = new Label();
    		gridPane.add(funkcja_interpolowana, 0, 1);
    		
    		funkcja_interpolowana.setText("w(x) = ");
    		for(int i = x.length-1; i>=0; i--){
    			System.out.println("a"+i+" = " +y[i] );
    			funkcja_interpolowana.setText(funkcja_interpolowana.getText() + y[i] + "x^" + i + " + ");
    		}
    		funkcja_interpolowana.setText(funkcja_interpolowana.getText().substring(0, funkcja_interpolowana.getText().length()-2));
    		
    		pom1 = new Label();
    		gridPane.add(pom1, 0, 2);
    		
    		odcieta2 = new TextField();
    		odcieta2.setPromptText("odcieta");
    		gridPane.add(odcieta2, 0, 3);
    		
    		oblicz = new Button();
    		oblicz.setText("oblicz");
    		gridPane.add(oblicz, 1, 3);
    		
    		pom2 = new Label();
    		gridPane.add(pom2, 0, 4);
    		
    		fx = new Label();
    		wx = new Label();
    		fx.setText("f(x) = ");
    		wx.setText("w(x) = ");
    		gridPane.add(fx, 0, 5);
    		gridPane.add(wx, 0, 6);
    		
    		delta = new Label();
    		delta.setText("\u2206 = ");
    		gridPane.add(delta, 0, 7);
    		
    		funkcja_bazowa.setWrapText(true);
    		funkcja_interpolowana.setWrapText(true);
    		fx.setWrapText(true);
    		wx.setWrapText(true);
    		delta.setWrapText(true);
    		
    		funkcja_bazowa.setMaxWidth(480);
    		funkcja_interpolowana.setMaxWidth(480);
    		fx.setMaxWidth(480);
    		wx.setMaxWidth(480);
    		delta.setMaxWidth(480);
    		
    		funkcja_bazowa.setTextAlignment(TextAlignment.JUSTIFY);
    		funkcja_interpolowana.setTextAlignment(TextAlignment.JUSTIFY);
    		fx.setTextAlignment(TextAlignment.JUSTIFY);
    		wx.setTextAlignment(TextAlignment.JUSTIFY);
    		delta.setTextAlignment(TextAlignment.JUSTIFY);
    		
    		
    		oblicz.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				if(!odcieta2.getText().isEmpty() && isNumeric2(odcieta2.getText())) {
    					double x = Double.parseDouble(odcieta2.getText());
    					double f, w;
    					
    					if(index == 0) {
    						f=Math.pow(x, p);
    			        }
    			        
    			        else if(index == 1) {
    			        	f=Math.pow(p, x);
    			        }

    			        else if(index == 2) {
    			        	f=p*Math.sin(x);
    			        }
    					
    			        else if(index == 3) {
    			        	f=p*Math.abs(x);
    			        }
    			        
    			        else {
    			        	f=0;
    			        	for(int t=0; t<funkcja_uzytkownika.size(); t++) {
    			        		f += Math.pow(x, t)*funkcja_uzytkownika.get(t);
    			        	}
    			        }
    			        fx.setText("f(x) = " + f);
    			        
    					w = 0;
    					for(int t=0; t<y1.length; t++) {
    						w += Math.pow(x, t)*y1[t];
    					}
    					wx.setText("w(x) = " + w);
    					
    					delta.setText("\u2206 = " + (f-w));
    				}
    			}
    		});
    		
    		
    		
    		for(double i=mn; i<=mx; i+=0.001) {
    			double fun = 0;
    			for(int w=0; w<y.length; w++) {
    				fun += Math.pow(i, w)*y[w];
    			}
    			series2.getData().add(new XYChart.Data(i, fun));
    		}
    		
    		XYChart.Series series3 = new XYChart.Series();
            series3.setName("punkty u¿ytkownika");
            //scatterChart.setCreateSymbols(true);
            for(double i: punkty_uzytkownika) {
            	double fun = 0;
    			for(int w=0; w<y.length; w++) {
    				fun += Math.pow(i, w)*y[w];
    			}
            	series3.getData().add(new XYChart.Data(i, fun));
            }
            
            Stage stage = new Stage();
            Stage stage2 = new Stage();
            Scene scene = new Scene(scatterChart,1024,768);
            scene.getStylesheets().add("/view/style.css");
            scatterChart.getData().addAll(series, series2, series3);
            
            stage.setScene(scene);
            stage.setTitle("Wykres");
            stage.show();
            
            
            Scene scene2 = new Scene(gridPane, 500, 500);
            stage2.setScene(scene2);
            stage2.setTitle("Dane");
            stage2.show();
            
            
        } else if(aryt==1) {
        	RealInterval[] x = new RealInterval[punkty_uzytkownika.size()];
        	for(int i=0; i<punkty_uzytkownika.size(); i++) {
        		x[i] = new RealInterval(punkty_uzytkownika.get(i));
        	}
        	
        	RealInterval[] y = new RealInterval[punkty_uzytkownika.size()];
        	for(int i=0; i<punkty_uzytkownika.size(); i++) {
        		if(index==0) {
        			y[i] = IAMath.evenPower(x[i], p);
        		}
        		else if(index==1) {
        			y[i] = IAMath.power(new RealInterval(p), x[i]);
        		}
        		else if(index==2) {
        			y[i] = IAMath.sin(x[i]);
        		}
        		else if(index==3) {
        			y[i] = IAMath.abs(x[i]);
        		}
        		else {
        			for(int w=0; w<funkcja_uzytkownika.size(); w++) {
        				y[i]=IAMath.add(y[i], IAMath.mul(IAMath.evenPower(x[i], w), new RealInterval(funkcja_uzytkownika.get(w))));
        			}
        		}
        	}
        	
        	y=interpolacja2(x,y);
        	y2=y;
        	
        	gridPane = new GridPane();
    		gridPane.setMinSize(400, 200); 
    	    gridPane.setPadding(new Insets(10, 10, 10, 10)); 
    	    gridPane.setVgap(5); 
    	    gridPane.setHgap(5);       
    	    gridPane.setAlignment(Pos.CENTER); 
    		
    	    funkcja_bazowa = new Label();
    	    funkcja_bazowa.setText("f(x) = "+series.getName());
    	    gridPane.add(funkcja_bazowa, 0, 0);
    	    
    		funkcja_interpolowana = new Label();
    		gridPane.add(funkcja_interpolowana, 0, 1);
    		
    		funkcja_interpolowana.setText("w([x]) = ");
    		for(int i = x.length-1; i>=0; i--){
    			funkcja_interpolowana.setText(funkcja_interpolowana.getText() + "["+y[i].lo+", "+y[i].hi + "]x^" + i + " + ");
    		}
    		funkcja_interpolowana.setText(funkcja_interpolowana.getText().substring(0, funkcja_interpolowana.getText().length()-2));
    		
    		pom1 = new Label();
    		gridPane.add(pom1, 0, 2);
    		
    		odcieta2 = new TextField();
    		odcieta2.setPromptText("odcieta");
    		gridPane.add(odcieta2, 0, 3);
    		
    		oblicz = new Button();
    		oblicz.setText("oblicz");
    		gridPane.add(oblicz, 1, 3);
    		
    		pom2 = new Label();
    		gridPane.add(pom2, 0, 4);
    		
    		fx = new Label();
    		wx = new Label();
    		fx.setText("f(x) = ");
    		wx.setText("w([x]) = ");
    		gridPane.add(fx, 0, 5);
    		gridPane.add(wx, 0, 6);
    		
    		delta = new Label();
    		delta.setText("\u2206 = ");
    		gridPane.add(delta, 0, 7);
    		
    		funkcja_bazowa.setWrapText(true);
    		funkcja_interpolowana.setWrapText(true);
    		fx.setWrapText(true);
    		wx.setWrapText(true);
    		delta.setWrapText(true);
    		
    		funkcja_bazowa.setMaxWidth(480);
    		funkcja_interpolowana.setMaxWidth(480);
    		fx.setMaxWidth(480);
    		wx.setMaxWidth(480);
    		delta.setMaxWidth(480);
    		
    		funkcja_bazowa.setTextAlignment(TextAlignment.JUSTIFY);
    		funkcja_interpolowana.setTextAlignment(TextAlignment.JUSTIFY);
    		fx.setTextAlignment(TextAlignment.JUSTIFY);
    		wx.setTextAlignment(TextAlignment.JUSTIFY);
    		delta.setTextAlignment(TextAlignment.JUSTIFY);
    		
    		oblicz.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				if(!odcieta2.getText().isEmpty() && isNumeric2(odcieta2.getText())) {
    					double x = Double.parseDouble(odcieta2.getText());
    					double f=0;
    					RealInterval w=new RealInterval(0);
    					
    					if(index == 0) {
    						f=Math.pow(x, p);
    			        }
    			        
    			        else if(index == 1) {
    			        	f=Math.pow(p, x);
    			        }
    			        
    			        else if(index == 2) {
    			        	f=p*Math.sin(x);
    			        }
    					
    			        else if(index == 3) {
    			        	f=p*Math.abs(x);
    			        }
    			        
    			        else {
    			        	f=0;
    			        	for(int t=0; t<funkcja_uzytkownika.size(); t++) {
    			        		f += Math.pow(x, t)*funkcja_uzytkownika.get(t);
    			        	}
    			        }
    			        fx.setText("f(x) = " + f);
    			        
    					for(int t=0; t<y2.length; t++) {
    						w = IAMath.add(w, IAMath.mul(new RealInterval(Math.pow(x, t)), y2[t]));
    						//w += Math.pow(x, t)*y1[t];
    					}
    					wx.setText("w([x]) = [" + w.lo + ", " + w.hi + "]");
    					
    					delta.setText("\u2206 = " + IAMath.sub(new RealInterval(f), w));
    				}
    			}
    		});
    		
    		for(double i=mn; i<=mx; i+=0.01) {
    			double fun = 0;
    			double fun2 = 0;
    			for(int w=0; w<y.length; w++) {
    				fun += Math.pow(i, w)*y[w].lo;
    				fun2 += Math.pow(i, w)*y[w].hi;
    			}
    			series2.getData().add(new XYChart.Data(i, fun));
    			series2.getData().add(new XYChart.Data(i, fun2));
    		}
    		
    		XYChart.Series series3 = new XYChart.Series();
            series3.setName("punkty u¿ytkownika");
            //scatterChart.setCreateSymbols(true);
            for(double i: punkty_uzytkownika) {
            	double fun = 0;
            	double fun2 = 0;
    			for(int w=0; w<y.length; w++) {
    				fun += Math.pow(i, w)*y[w].lo;
    				fun2 += Math.pow(i, w)*y[w].hi;
    			}
    			series3.getData().add(new XYChart.Data(i, fun));
    			series3.getData().add(new XYChart.Data(i, fun2));
            }
            
            Stage stage = new Stage();
            Stage stage2 = new Stage();
            Scene scene = new Scene(scatterChart,1024,768);
            scene.getStylesheets().add("/view/style.css");
            scatterChart.getData().addAll(series, series2, series3);
            
            stage.setScene(scene);
            stage.setTitle("Wykres");
            stage.show();
            
            
            Scene scene2 = new Scene(gridPane, 500, 500);
            stage2.setScene(scene2);
            stage2.setTitle("Dane");
            stage2.show();
        	
        }
		
	}
	
	@Override
    public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("it works!");
		punkty.setText("x \u2208 { }");
		
		funkcja_uzytkownika = new ArrayList<>();
		punkty_uzytkownika = new ArrayList<>();
		
		wylaczFunkcje();
		wylaczPunkty();
		ilosc_punktow.setDisable(false);
		zatwierdz3.setDisable(false);
		
		interpoluj.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!funkcja.getText().equals("f(x) =") && !punkty.getText().equals("x \u2208 { }") && zatwierdz5.isDisable() && aryt!=-1) {
					try {
						wykres();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		zatwierdz.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!stopien_wielomianu.getText().isEmpty() && isNumeric(stopien_wielomianu.getText())) {
					st = Integer.parseInt(stopien_wielomianu.getText());
					stopien_wielomianu.setDisable(true);
					zatwierdz.setDisable(true);
				}
			}
		});
		
		dodaj_wspolczynnik.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!wspolczynnik.getText().isEmpty() && st > 0 && isNumeric2(wspolczynnik.getText())) {
					st--;
					funkcja_uzytkownika.add(Double.parseDouble(wspolczynnik.getText()));
					funkcja.setText(funkcja.getText() + " " + wspolczynnik.getText() + "x^" + st);
					if(st > 0)funkcja.setText(funkcja.getText() + " +");
					wspolczynnik.clear();					
				}
				
				if(st == 0) {
					wylaczFunkcje();
					Collections.reverse(funkcja_uzytkownika);
				}
			}
		});
		
		ktora_domyslna.setItems(FXCollections.observableArrayList("x^p", "p^x", "p*sin(x)", "p*|x|"));
		
		ktora_domyslna.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number new_value) {
				index = (int) new_value;
			}
		});
		
		zatwierdz2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!parametr.getText().isEmpty() && isNumeric2(parametr.getText())) {
					
					if(index == 0) {
						funkcja.setText(funkcja.getText() + " x^" + parametr.getText());
					}
					
					if(index == 1) {
						funkcja.setText(funkcja.getText() + " " + parametr.getText() + "^x");
					}
					
					if(index == 2) {
						funkcja.setText(funkcja.getText() + " " + parametr.getText() + "*sin(x)");
					}
					
					if(index == 3) {
						funkcja.setText(funkcja.getText() + " " + parametr.getText() + "*|x|");
					}
					
					p = Double.parseDouble(parametr.getText());
					wylaczFunkcje();
				}
			}
		});
		
		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ktora_domyslna.setDisable(false);
				parametr.setDisable(false);
				zatwierdz2.setDisable(false);
				dodaj_wspolczynnik.setDisable(false);
				wspolczynnik.setDisable(false);
				stopien_wielomianu.setDisable(false);
				zatwierdz.setDisable(false);
				funkcja.setText("f(x) =");
				stopien_wielomianu.clear();
				wspolczynnik.clear();
				parametr.clear();
				funkcja_uzytkownika.clear();
			}
		});
		
		ToggleGroup tg = new ToggleGroup();
		z_reki_funkcja.setToggleGroup(tg);
		domyslna.setToggleGroup(tg);
		
		ToggleGroup tg2 = new ToggleGroup();
		z_reki_punkty.setToggleGroup(tg2);
		rownoodlegle.setToggleGroup(tg2);
		czebyszewa.setToggleGroup(tg2);
		
		zatwierdz3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!ilosc_punktow.getText().isEmpty() && isNumeric(ilosc_punktow.getText())) {
					pkt = Integer.parseInt(ilosc_punktow.getText());
					if(pkt>=2) {
						pkt2 = pkt;
						ilosc_punktow.setDisable(true);
						zatwierdz3.setDisable(true);
					}
					
				}
			}
		});
		
		zatwierdz4.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!odleglosc.getText().isEmpty() && isNumeric2(odleglosc.getText()) && pkt > 0) {
					odl = Double.parseDouble(odleglosc.getText());
					wylaczPunkty();
					
					punkty.setText("x \u2208 { ");
					for(int i=0; i<pkt; i++) {
						punkty_uzytkownika.add(i*odl);
						punkty.setText(punkty.getText() + i*odl + ", ");
					}
					punkty.setText(punkty.getText() + "}");
					punkty.setText(punkty.getText().replace(", }", " }"));
					Collections.sort(punkty_uzytkownika);
				}
			}
		});
		
		dodaj_punkt.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(!odcieta.getText().isEmpty() && pkt2 > 0 && isNumeric2(odcieta.getText())) {
					pkt2--;
					punkty_uzytkownika.add(Double.parseDouble(odcieta.getText()));
					punkty.setText(punkty.getText().substring(0, punkty.getText().length()-1) + odcieta.getText() + " }");
					if(pkt2>0)punkty.setText(punkty.getText().replace(" }", ", }"));
					odcieta.clear();
				}
				
				if(pkt2 == 0) {
					wylaczPunkty();
				}
				
				Collections.sort(punkty_uzytkownika);
			}
		});
		
		reset2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ilosc_punktow.setDisable(false);
				odleglosc.setDisable(false);
				zatwierdz3.setDisable(false);
				zatwierdz4.setDisable(false);
				dodaj_punkt.setDisable(false);
				odcieta.setDisable(false);
				zatwierdz6.setDisable(false);
				punkty.setText("x \u2208 { }");
				ilosc_punktow.clear();
				odcieta.clear();
				odleglosc.clear();
				punkty_uzytkownika.clear();
			}
		});
		
		reset3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				min.clear();
				max.clear();
				min.setDisable(false);
				max.setDisable(false);
				zatwierdz5.setDisable(false);
			}
		});
		
		zatwierdz5.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				if(!min.getText().isEmpty() && !max.getText().isEmpty() && isNumeric2(min.getText()) && isNumeric2(max.getText())) {
					mn = Double.parseDouble(min.getText());
					mx = Double.parseDouble(max.getText());
					
					if(mn < mx) {
						min.setDisable(true);
						max.setDisable(true);
						zatwierdz5.setDisable(true);
					}
				}
			}
		});
		
		zatwierdz6.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				double l;
				
				for(int j=1; j<=pkt; j++) {
					l = Math.cos(Math.PI*(2*j-1)/(2*pkt));
					punkty_uzytkownika.add(l);
					if(pkt < 2)l=0;
					punkty.setText(punkty.getText().substring(0, punkty.getText().length()-1) + l + " }");
					if(j < pkt)punkty.setText(punkty.getText().replace(" }", ", }"));
				}
				
				Collections.sort(punkty_uzytkownika);
				wylaczPunkty();
				
			}
		});
		
		tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		         if (tg.getSelectedToggle() != null && funkcja.getText().equals("f(x) =")) {
		        	 RadioButton chk = (RadioButton)new_toggle.getToggleGroup().getSelectedToggle(); 
		             //System.out.println("Selected Radio Button - "+chk.getText());
		        	 wylaczFunkcje();
		        	 if(chk.getText().equals(z_reki_funkcja.getText())) {
		        		 stopien_wielomianu.setDisable(false);
		        		 zatwierdz.setDisable(false);
		        		 wspolczynnik.setDisable(false);
		        		 dodaj_wspolczynnik.setDisable(false);
		        		 
		        	 } else if(chk.getText().equals(domyslna.getText())) {
		        		 ktora_domyslna.setDisable(false);
		        		 parametr.setDisable(false);
		        		 zatwierdz2.setDisable(false);
		        		 
		        	 }
		         }

		     } 
		});
		
		tg2.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
		    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		         if (tg2.getSelectedToggle() != null && punkty.getText().equals("x \u2208 { }")) {
		        	 RadioButton chk = (RadioButton)new_toggle.getToggleGroup().getSelectedToggle(); 
		             //System.out.println("Selected Radio Button - "+chk.getText());
		        	 wylaczPunkty();
		        	 if(chk.getText().equals(z_reki_punkty.getText())) {
		        		 odcieta.setDisable(false);
		        		 dodaj_punkt.setDisable(false);
		        		 
		        	 } else if(chk.getText().equals(rownoodlegle.getText())) {
		        		 odleglosc.setDisable(false);
		        		 zatwierdz4.setDisable(false);
		        		 
		        	 } else if(chk.getText().equals(czebyszewa.getText())) {
		        		 zatwierdz6.setDisable(false);
		        		 
		        	 }
		         }

		     } 
		});
		
		arytmetyka.setItems(FXCollections.observableArrayList("arytmetyka zmiennoprzecinkowa", "arytmetyka przedzia³owa"));
		
		arytmetyka.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number new_value) {
				aryt = (int) new_value;
				System.out.println(aryt);
			}
		});
	} 
	
	private void wylaczFunkcje() {
		ktora_domyslna.setDisable(true);
		parametr.setDisable(true);
		zatwierdz2.setDisable(true);
		dodaj_wspolczynnik.setDisable(true);
		wspolczynnik.setDisable(true);
		stopien_wielomianu.setDisable(true);
		zatwierdz.setDisable(true);
	}
	
	private void wylaczPunkty() {
		ilosc_punktow.setDisable(true);
		odleglosc.setDisable(true);
		zatwierdz3.setDisable(true);
		zatwierdz4.setDisable(true);
		dodaj_punkt.setDisable(true);
		odcieta.setDisable(true);
		zatwierdz6.setDisable(true);
	}
	
	private static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	private static boolean isNumeric2(String number)
	{
		String decimalPattern = "([0-9]*)\\.([0-9]*)";
		boolean match = Pattern.matches(decimalPattern, number);
		boolean match2 = Pattern.matches(decimalPattern, number.substring(1));
		
		return match || match2;
	}
	
	private static double[] interpolacja(double[] x, double[] y) {
		int st=0, i=-1, n=x.length;
		
		do {
			i++;
			for(int k=i+1;k<n;k++) {
				if(x[i]==x[k])st=42;
			}
		} while(i==n-1 || st==42);
		
		if(st==0) {
			for(int k=1; k<n; k++) {
				for(i=0; i<n-k; i++) {
					y[i]=(y[i+1]-y[i])/(x[i+k]-x[i]);
				}
			}
			
			for(i=1;i<n;i++){
				for(int j=i;j>=1;j--){
					y[j]-=y[j-1]*x[i];
				}
			}
		}
		for (int left = 0, right = y.length - 1; left < right; left++, right--) {
	        // swap the values at the left and right indices
	        double temp = y[left];
	        y[left] = y[right];
	        y[right] = temp;
	    }
		return y;
	}
	
	private static RealInterval[] interpolacja2(RealInterval[] x, RealInterval[] y) {
		int st=0, i=-1, n=x.length;
		
		do {
			i++;
			for(int k=i+1;k<n;k++) {
				if(x[i].equals(x[k]))st=42;
			}
		} while(i==n-1 || st==42);
		
		if(st==0) {
			for(int k=1; k<n; k++) {
				for(i=0; i<n-k; i++) {
					y[i]=IAMath.div(IAMath.sub(y[i+1], y[i]), IAMath.sub(x[i+k], x[i]));
					//y[i]=(y[i+1]-y[i])/(x[i+k]-x[i]);
					//System.out.println("y[i] = ["+y[i].lo + ", " +y[i].hi+"]");
				}
			}
			//System.out.println();
			
			for(i=1;i<n;i++){
				for(int j=i;j>=1;j--){
					y[j]=IAMath.sub(y[j], IAMath.mul(y[j-1],x[i]));
					//y[j]=y[j]-y[j-1]*x[i];
					//System.out.println("y[j] = ["+y[j].lo + ", " +y[j].hi+"]");
				}
			}
			//System.out.println();
		}
		for (int left = 0, right = y.length - 1; left < right; left++, right--) {
	        // swap the values at the left and right indices
	        RealInterval temp = y[left];
	        y[left] = y[right];
	        y[right] = temp;
	    }
		
		return y;
	}
	
    @Override
    public void start(Stage stage) throws Exception {      	
		Parent parent = (Parent) FXMLLoader.load(getClass().getResource("/view/MainPane.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Interpolacja Newtona");
        stage.show();
    }
     
    public static void main(String[] args) {
        launch(args);
    }
  

}
