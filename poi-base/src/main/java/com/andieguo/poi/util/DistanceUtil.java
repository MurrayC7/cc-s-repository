package com.andieguo.poi.util;

public class DistanceUtil {
	//http://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
	public static void main(String[] args) {
//		double distance = distVincenty(34.80033, 113.6598733, 34.76, 113.65);
		double distance = computeDistance(31.81, 115.42, 34.76, 113.65);
		double distance2 = getDistance(31.81, 115.42, 34.76, 113.65);
		System.out.println(distance);
		System.out.println(distance2);
	}
	
	/*
	 * Calculate distance between two points in latitude and longitude taking
	 * into account height difference. If you are not interested in height
	 * difference pass 0.0. Uses Haversine method as its base.
	 * 
	 * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
	 * el2 End altitude in meters
	 * @returns Distance in Meters
	 */
	public static double distance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    Double latDistance = Math.toRadians(lat2 - lat1);
	    Double lonDistance = Math.toRadians(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}

	public static double getDistance(double lat1,double longt1 , double lat2,double longt2
	            ) {
	        double PI = 3.14159265358979323; // 圆周率
	        double R = 6371229; // 地球的半径

	        double x, y, distance;
	        x = (longt2 - longt1) * PI * R
	                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
	        y = (lat2 - lat1) * PI * R / 180;
	        distance = Math.hypot(x, y);

	        return distance;
	    }
	
	public static double computeDistance(double lat1, double lon1,
	        double lat2, double lon2) {
	        // Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
	        // using the "Inverse Formula" (section 4)

	        int MAXITERS = 20;
	        // Convert lat/long to radians
	        lat1 *= Math.PI / 180.0;
	        lat2 *= Math.PI / 180.0;
	        lon1 *= Math.PI / 180.0;
	        lon2 *= Math.PI / 180.0;

	        double a = 6378137.0; // WGS84 major axis
	        double b = 6356752.3142; // WGS84 semi-major axis
	        double f = (a - b) / a;
	        double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

	        double L = lon2 - lon1;
	        double A = 0.0;
	        double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
	        double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

	        double cosU1 = Math.cos(U1);
	        double cosU2 = Math.cos(U2);
	        double sinU1 = Math.sin(U1);
	        double sinU2 = Math.sin(U2);
	        double cosU1cosU2 = cosU1 * cosU2;
	        double sinU1sinU2 = sinU1 * sinU2;

	        double sigma = 0.0;
	        double deltaSigma = 0.0;
	        double cosSqAlpha = 0.0;
	        double cos2SM = 0.0;
	        double cosSigma = 0.0;
	        double sinSigma = 0.0;
	        double cosLambda = 0.0;
	        double sinLambda = 0.0;

	        double lambda = L; // initial guess
	        for (int iter = 0; iter < MAXITERS; iter++) {
	            double lambdaOrig = lambda;
	            cosLambda = Math.cos(lambda);
	            sinLambda = Math.sin(lambda);
	            double t1 = cosU2 * sinLambda;
	            double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
	            double sinSqSigma = t1 * t1 + t2 * t2; // (14)
	            sinSigma = Math.sqrt(sinSqSigma);
	            cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
	            sigma = Math.atan2(sinSigma, cosSigma); // (16)
	            double sinAlpha = (sinSigma == 0) ? 0.0 :
	                cosU1cosU2 * sinLambda / sinSigma; // (17)
	            cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
	            cos2SM = (cosSqAlpha == 0) ? 0.0 :
	                cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

	            double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
	            A = 1 + (uSquared / 16384.0) * // (3)
	                (4096.0 + uSquared *
	                 (-768 + uSquared * (320.0 - 175.0 * uSquared)));
	            double B = (uSquared / 1024.0) * // (4)
	                (256.0 + uSquared *
	                 (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
	            double C = (f / 16.0) *
	                cosSqAlpha *
	                (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
	            double cos2SMSq = cos2SM * cos2SM;
	            deltaSigma = B * sinSigma * // (6)
	                (cos2SM + (B / 4.0) *
	                 (cosSigma * (-1.0 + 2.0 * cos2SMSq) -
	                  (B / 6.0) * cos2SM *
	                  (-3.0 + 4.0 * sinSigma * sinSigma) *
	                  (-3.0 + 4.0 * cos2SMSq)));

	            lambda = L +
	                (1.0 - C) * f * sinAlpha *
	                (sigma + C * sinSigma *
	                 (cos2SM + C * cosSigma *
	                  (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

	            double delta = (lambda - lambdaOrig) / lambda;
	            if (Math.abs(delta) < 1.0e-12) {
	                break;
	            }
	        }

	        return  b * A * (sigma - deltaSigma);
	    }

}
