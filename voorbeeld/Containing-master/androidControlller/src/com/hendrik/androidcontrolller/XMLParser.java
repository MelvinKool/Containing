/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hendrik.androidcontrolller;

import android.util.Xml;
import containing.xml.CustomVector3f;
import containingcontroller.Container;
import containingcontroller.TransportTypes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 
 * @author Hendrik
 */
public class XMLParser {

	/**
	 * Parse inputstream from xml file to list of containers
	 * 
	 * @param File
	 *            inputstream from xmlfile
	 * @return list of read containers
	 */
	static public List<Container> parseXMLFile(XmlPullParser parser) {
		{
			ArrayList<Container> containersList = new ArrayList<Container>();
			try {

				String currentTag = null;
				String tagContent = null;
				Stack<String> sk = new Stack<String>();
				Calendar c = Calendar.getInstance();
				CustomVector3f p = new CustomVector3f();

				Container currentContainer = null;
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					eventType = parser.next();
			
					switch (eventType) {
					
					case XmlPullParser.START_DOCUMENT:

						if ("recordset".equals(parser.getName())) {
							containersList = new ArrayList<Container>();
						}

					
						break;
					case XmlPullParser.START_TAG:
						if ("record".equals(parser.getName())) {
							currentContainer = new Container();
							currentContainer.setId(parser.getAttributeValue(0));
						}
						sk.push(parser.getName());
						break;
					case XmlPullParser.TEXT:
						tagContent = parser.getText().trim();
						break;

					case XmlPullParser.END_TAG:
						sk.pop();
						String t = parser.getName().toLowerCase();
						if (t.equals("record")) {
							containersList.add(currentContainer);
						} else if (t.equals("d")) {
							c = Calendar.getInstance();
							c.set(Calendar.DAY_OF_MONTH,
									Integer.parseInt(tagContent));
						} else if (t.equals("m")) {
							c.set(Calendar.MONTH,
									Integer.parseInt(tagContent) - 1);
						} else if (t.equals("j")) {
							c.set(Calendar.YEAR,
									Integer.parseInt(tagContent) + 2000);
						} else if (t.equals("van")) {
							String[] time = tagContent.split("\\.");
							c.set(Calendar.HOUR, Integer.parseInt(time[0]));
							c.set(Calendar.MINUTE, Integer.parseInt(time[1]));
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);
						} else if (t.equals("tijd")) {
							if (sk.peek().equalsIgnoreCase("aankomst")) {
								currentContainer.setDateArrival(c.getTime());// wat
																				// te
																				// doen
																				// met
																				// tijd
							} else {
								currentContainer.setDateDeparture(c.getTime());
							}
						} else if (t.equals("soort_vervoer")) {
							if (sk.peek().equalsIgnoreCase("aankomst")) {
								currentContainer
										.setTransportTypeArrival(TransportTypes
												.getTransportType(tagContent));
							} else {
								currentContainer
										.setTransportTypeDeparture(TransportTypes
												.getTransportType(tagContent));
							}
						} else if (t.equals("bedrijf")) {
							if (sk.peek().equalsIgnoreCase("aankomst")) {
								currentContainer
										.setCargoCompanyArrival(tagContent);
							} else if (sk.peek().equalsIgnoreCase("vertrek")) {
								currentContainer
										.setCargoCompanyDeparture(tagContent);
							}

						} else if (t.equals("x")) {
							p = new CustomVector3f();
							p.x = Integer.parseInt(tagContent);
							if (currentContainer.getTransportTypeArrival() == TransportTypes.LORREY) {
								p.x = 0;
							}

						} else if (t.equals("y")) {
							p.z = Integer.parseInt(tagContent);

						} else if (t.equals("z")) {
							p.y = Integer.parseInt(tagContent);

						} else if (t.equals("positie")) {
							currentContainer.setPosition(p);
						} else if (t.equals("naam")) {
							if (sk.peek().equalsIgnoreCase("eigenaar")) {
								currentContainer.setOwner(tagContent);
							} else {
								currentContainer.setContents(tagContent);
							}
						} else if (t.equals("containernr")) {
							currentContainer.setContainerNumber(Integer
									.parseInt(tagContent));
						} else if (t.equals("l")) {
							currentContainer.setLenght(tagContent);
						} else if (t.equals("h")) {
							currentContainer.setHeight(tagContent);
						} else if (t.equals("b")) {
							currentContainer.setWidth(tagContent);
						} else if (t.equals("leeg")) {
							currentContainer.setWeightEmpty(Integer
									.parseInt(tagContent));
						} else if (t.equals("inhoud")) {
							if (sk.peek().equalsIgnoreCase("aankomst")) {
								if (!tagContent.isEmpty()) {
									currentContainer.setWeightLoaded(Integer
											.parseInt(tagContent));
								}
							}
						} else if (t.equals("soort")) {
							currentContainer.setContentType(tagContent);
						} else if (t.equals("gevaar")) {
							currentContainer.setContentDanger(tagContent);
						} else if (t.equals("iso")) {
							currentContainer.setIso(tagContent);
						}

					}
					
				}
			} catch (Exception e) {
				int a = 0;
				a = 2;
			}

			return containersList;

		}

	}
}
