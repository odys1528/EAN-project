package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import Jama.Matrix;
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
import javafx.stage.Stage;

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
	
	
	private void wykres() throws IOException {
		//PIERWSZY WYKRES----------------------------------------------------------------------
		chartX = new NumberAxis();
        chartY = new NumberAxis();
        String[] wykres = {"x^", "^x", "*sin(x)"};
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
        
        else {
        	series.setName(funkcja.getText());
        	for(double i=mn; i<=mx; i+=0.01) {
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
        
        double[] x = new double[punkty_uzytkownika.size()];
		for(int i=0; i<punkty_uzytkownika.size(); i++) {
			x[i]=punkty_uzytkownika.get(i);
		}
		
		double[][] y = new double[punkty_uzytkownika.size()][1];
		for(int i=0; i<punkty_uzytkownika.size(); i++){
			if(index == 0) {
				y[i][0] = Math.pow(punkty_uzytkownika.get(i), p);
			}
			else if(index == 1) {
				y[i][0] = Math.pow(p, punkty_uzytkownika.get(i));
			}
			else if(index == 2) {
				y[i][0] = Math.sin(punkty_uzytkownika.get(i))*p;
			}
			else {
				for(int w=0; w<funkcja_uzytkownika.size(); w++) {
        			y[i][0] += Math.pow(i, w)*funkcja_uzytkownika.get(w);
        		}
			}
		}
        
		Matrix g = polynomialInterpolation(x,y);
		
		gridPane = new GridPane();
		gridPane.setMinSize(400, 200); 
	    gridPane.setPadding(new Insets(10, 10, 10, 10)); 
	    gridPane.setVgap(5); 
	    gridPane.setHgap(5);       
	    gridPane.setAlignment(Pos.CENTER); 
		
	    funkcja_bazowa = new Label();
	    funkcja_bazowa.setText("f(x) = " + series.getName());
	    gridPane.add(funkcja_bazowa, 0, 0);
	    
		funkcja_interpolowana = new Label();
		gridPane.add(funkcja_interpolowana, 0, 1);
		
		funkcja_interpolowana.setText("w(x) = ");
		for(int i = x.length-1; i>=0; i--){
			System.out.println("a"+i+" = " +g.get(i, 0) );
			funkcja_interpolowana.setText(funkcja_interpolowana.getText() + g.get(i,0) + "x^" + i + " + ");
		}
		funkcja_interpolowana.setText(funkcja_interpolowana.getText().substring(0, funkcja_interpolowana.getText().length()-2));
		
		odcieta2 = new TextField();
		odcieta2.setPromptText("odcieta");
		gridPane.add(odcieta2, 0, 2);
		
		oblicz = new Button();
		oblicz.setText("oblicz");
		gridPane.add(oblicz, 1, 2);
		
		fx = new Label();
		wx = new Label();
		fx.setText("f(x) = ");
		wx.setText("w(x) = ");
		gridPane.add(fx, 0, 3);
		gridPane.add(wx, 0, 4);
		
		
		
		for(double i=mn; i<=mx; i+=0.01) {
			double fun = 0;
			for(int w=0; w<g.getRowDimension(); w++) {
				fun += Math.pow(i, w)*g.get(w, 0);
			}
			series2.getData().add(new XYChart.Data(i, fun));
		}
		
		XYChart.Series series3 = new XYChart.Series();
        series3.setName("punkty u¿ytkownika");
        //scatterChart.setCreateSymbols(true);
        for(double i: punkty_uzytkownika) {
        	double fun = 0;
			for(int w=0; w<g.getRowDimension(); w++) {
				fun += Math.pow(i, w)*g.get(w, 0);
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
        
        
        Scene scene2 = new Scene(gridPane, 400, 400);
        stage2.setScene(scene2);
        stage2.setTitle("Dane");
        stage2.show();
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
		
		ktora_domyslna.setItems(FXCollections.observableArrayList("x^p", "p^x", "p*sin(x)"));
		
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
					pkt2 = pkt;
					ilosc_punktow.setDisable(true);
					zatwierdz3.setDisable(true);
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
  
    public static double iloraz(List<Double> l) {
    	//{"x^p", "p^x", "p*sin(x)"}
    	if(l.size() == 2) {
    		if(index == 0) {
    			return (Math.pow(l.get(1), p) - Math.pow(l.get(0), p)) / (l.get(1) - l.get(0));
    		}
    		if(index == 1) {
    			return (Math.pow(p, l.get(1)) - Math.pow(p, l.get(0))) / (l.get(1) - l.get(0));
    		}
    		if(index == 2) {
    			return (p*Math.sin(l.get(1)) - p*Math.sin(l.get(0))) / (l.get(1) - l.get(0));
    		}
    	}
    	
    	return (iloraz(l.subList(1, l.size()))- iloraz(l.subList(0, l.size()-1))) / (l.get(l.size()-1) - l.get(0));
    }
    
    public static Matrix polynomialInterpolation(double[] x, double[][] y){
		 
		int n = x.length;
		Matrix A = new Matrix(n,n);
		for(int i =0; i< n; i++)
			for(int j = 0; j< n; j++)
				A.set(i, j, Math.pow(x[i], j));
		 
		Matrix a = A.solve(new Matrix(y));
		return a;
	}

}
