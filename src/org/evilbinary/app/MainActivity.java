/* Copyright (C) 2015 evilbinary.
 * rootdebug@163.com
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.evilbinary.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;

import org.evilbinary.highliter.HighlightEditText;
import org.evilbinary.highliter.R;
import org.evilbinary.highliter.R.id;
import org.evilbinary.highliter.R.menu;
import org.evilbinary.highliter.R.string;
import org.evilbinary.managers.Configure;
import org.evilbinary.managers.ConfigureManager;
import org.evilbinary.utils.DirUtil;
import org.evilbinary.utils.FileUtil;
import org.evilbinary.utils.Logger;
import org.evilbinary.utils.ZipUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
//import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ConfigureManager mConfigureManager;
	private HighlightEditText mHighlightEdit;
	private Configure mConf;
	private String mTextcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		mConfigureManager = new ConfigureManager(this);
		mConfigureManager.exractDefaultConfigure();
		mConf = mConfigureManager.getDefaultConfigure();
		mConf.mTheme = "fine_blue";// solarized-light vampire
		mConf.mLanguage = "python";

		final LinearLayout linearLayout = new LinearLayout(this);
		mHighlightEdit = new HighlightEditText(this, mConf);

		ActionBar actionBar = this.getSupportActionBar();

		try {
			String text = FileUtil.readFromAssetsFile(this, "test.html");
			mTextcode = FileUtil.readFromAssetsFile(this, "fib.py");
			mHighlightEdit.setSource(mTextcode);
			// hi.setHtml(text);

			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.addView(mHighlightEdit);

			setContentView(linearLayout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mymenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (item.isCheckable()) {
			item.setChecked(true);
		}
		switch (id) {
		case R.id.font10:
			mConf.mSettings.TEXT_SIZE = 10 * 2;
			break;
		case R.id.font12:
			mConf.mSettings.TEXT_SIZE = 12 * 2;
			break;
		case R.id.font14:
			mConf.mSettings.TEXT_SIZE = 14 * 2;
			break;
		case R.id.font16:
			mConf.mSettings.TEXT_SIZE = 16 * 2;
			break;
		case R.id.font18:
			mConf.mSettings.TEXT_SIZE = 18 * 2;
			break;
		case R.id.font_default:
			mConf.mSettings.TEXT_SIZE = mConf.mFontSize;
			break;

		case R.id.c:
			mConf.mLanguage = "c";
			break;
		case R.id.python:
			mConf.mLanguage = "python";
			break;
		case R.id.lisp:
			mConf.mLanguage = "lisp";
			break;

		case R.id.molokai:
			mConf.mTheme = "molokai";
			break;
		case R.id.fine_blue:
			mConf.mTheme = "fine_blue";
			break;
		case R.id.vampire:
			mConf.mTheme = "vampire";
			break;
		case R.id.solarized_light:
			mConf.mTheme = "solarized-light";
			break;

		case R.id.abap4: mConf.mLanguage = "abap4";break;
		case R.id.abc: mConf.mLanguage = "abc";break;
		case R.id.abnf: mConf.mLanguage = "abnf";break;
		case R.id.actionscript: mConf.mLanguage = "actionscript";break;
		case R.id.ada: mConf.mLanguage = "ada";break;
		case R.id.agda: mConf.mLanguage = "agda";break;
		case R.id.algol: mConf.mLanguage = "algol";break;
		case R.id.ampl: mConf.mLanguage = "ampl";break;
		case R.id.amtrix: mConf.mLanguage = "amtrix";break;
		case R.id.applescript: mConf.mLanguage = "applescript";break;
		case R.id.arc: mConf.mLanguage = "arc";break;
		case R.id.arm: mConf.mLanguage = "arm";break;
		case R.id.as400cl: mConf.mLanguage = "as400cl";break;
		case R.id.ascend: mConf.mLanguage = "ascend";break;
		case R.id.asp: mConf.mLanguage = "asp";break;
		case R.id.aspect: mConf.mLanguage = "aspect";break;
		case R.id.assembler: mConf.mLanguage = "assembler";break;
		case R.id.ats: mConf.mLanguage = "ats";break;
		case R.id.autohotkey: mConf.mLanguage = "autohotkey";break;
		case R.id.autoit: mConf.mLanguage = "autoit";break;
		case R.id.avenue: mConf.mLanguage = "avenue";break;
		case R.id.awk: mConf.mLanguage = "awk";break;
		case R.id.bat: mConf.mLanguage = "bat";break;
		case R.id.bbcode: mConf.mLanguage = "bbcode";break;
		case R.id.bcpl: mConf.mLanguage = "bcpl";break;
		case R.id.bibtex: mConf.mLanguage = "bibtex";break;
		case R.id.biferno: mConf.mLanguage = "biferno";break;
		case R.id.bison: mConf.mLanguage = "bison";break;
		case R.id.blitzbasic: mConf.mLanguage = "blitzbasic";break;
		case R.id.bms: mConf.mLanguage = "bms";break;
		case R.id.bnf: mConf.mLanguage = "bnf";break;
		case R.id.boo: mConf.mLanguage = "boo";break;
		case R.id.ceylon: mConf.mLanguage = "ceylon";break;
		case R.id.charmm: mConf.mLanguage = "charmm";break;
		case R.id.chill: mConf.mLanguage = "chill";break;
		case R.id.clean: mConf.mLanguage = "clean";break;
		case R.id.clearbasic: mConf.mLanguage = "clearbasic";break;
		case R.id.clipper: mConf.mLanguage = "clipper";break;
		case R.id.clojure: mConf.mLanguage = "clojure";break;
		case R.id.clp: mConf.mLanguage = "clp";break;
		case R.id.cobol: mConf.mLanguage = "cobol";break;
		case R.id.coldfusion: mConf.mLanguage = "coldfusion";break;
		case R.id.conf: mConf.mLanguage = "conf";break;
		case R.id.crk: mConf.mLanguage = "crk";break;
		case R.id.csharp: mConf.mLanguage = "csharp";break;
		case R.id.css: mConf.mLanguage = "css";break;
		case R.id.d: mConf.mLanguage = "d";break;
		case R.id.dart: mConf.mLanguage = "dart";break;
		case R.id.diff: mConf.mLanguage = "diff";break;
		case R.id.dylan: mConf.mLanguage = "dylan";break;
		case R.id.ebnf: mConf.mLanguage = "ebnf";break;
		case R.id.eiffel: mConf.mLanguage = "eiffel";break;
		case R.id.erlang: mConf.mLanguage = "erlang";break;
		case R.id.euphoria: mConf.mLanguage = "euphoria";break;
		case R.id.express: mConf.mLanguage = "express";break;
		case R.id.fame: mConf.mLanguage = "fame";break;
		case R.id.felix: mConf.mLanguage = "felix";break;
		case R.id.fortran77: mConf.mLanguage = "fortran77";break;
		case R.id.fortran90: mConf.mLanguage = "fortran90";break;
		case R.id.frink: mConf.mLanguage = "frink";break;
		case R.id.fsharp: mConf.mLanguage = "fsharp";break;
		case R.id.fx: mConf.mLanguage = "fx";break;
		case R.id.gambas: mConf.mLanguage = "gambas";break;
		case R.id.gdb: mConf.mLanguage = "gdb";break;
		case R.id.go: mConf.mLanguage = "go";break;
		case R.id.graphviz: mConf.mLanguage = "graphviz";break;
		case R.id.haskell: mConf.mLanguage = "haskell";break;
		case R.id.haxe: mConf.mLanguage = "haxe";break;
		case R.id.hcl: mConf.mLanguage = "hcl";break;
		case R.id.html: mConf.mLanguage = "html";break;
		case R.id.httpd: mConf.mLanguage = "httpd";break;
		case R.id.icon: mConf.mLanguage = "icon";break;
		case R.id.idl: mConf.mLanguage = "idl";break;
		case R.id.idlang: mConf.mLanguage = "idlang";break;
		case R.id.inc_luatex: mConf.mLanguage = "inc_luatex";break;
		case R.id.informix: mConf.mLanguage = "informix";break;
		case R.id.ini: mConf.mLanguage = "ini";break;
		case R.id.innosetup: mConf.mLanguage = "innosetup";break;
		case R.id.interlis: mConf.mLanguage = "interlis";break;
		case R.id.io: mConf.mLanguage = "io";break;
		case R.id.jasmin: mConf.mLanguage = "jasmin";break;
		case R.id.java: mConf.mLanguage = "java";break;
		case R.id.js: mConf.mLanguage = "js";break;
		case R.id.jsp: mConf.mLanguage = "jsp";break;
		case R.id.ldif: mConf.mLanguage = "ldif";break;
		case R.id.less: mConf.mLanguage = "less";break;
		case R.id.lhs: mConf.mLanguage = "lhs";break;
		case R.id.lilypond: mConf.mLanguage = "lilypond";break;
		case R.id.limbo: mConf.mLanguage = "limbo";break;
		case R.id.lindenscript: mConf.mLanguage = "lindenscript";break;
		case R.id.logtalk: mConf.mLanguage = "logtalk";break;
		case R.id.lotos: mConf.mLanguage = "lotos";break;
		case R.id.lotus: mConf.mLanguage = "lotus";break;
		case R.id.lua: mConf.mLanguage = "lua";break;
		case R.id.luban: mConf.mLanguage = "luban";break;
		case R.id.make: mConf.mLanguage = "make";break;
		case R.id.maple: mConf.mLanguage = "maple";break;
		case R.id.matlab: mConf.mLanguage = "matlab";break;
		case R.id.maya: mConf.mLanguage = "maya";break;
		case R.id.mercury: mConf.mLanguage = "mercury";break;
		case R.id.miranda: mConf.mLanguage = "miranda";break;
		case R.id.mod2: mConf.mLanguage = "mod2";break;
		case R.id.mod3: mConf.mLanguage = "mod3";break;
		case R.id.modelica: mConf.mLanguage = "modelica";break;
		case R.id.moon: mConf.mLanguage = "moon";break;
		case R.id.ms: mConf.mLanguage = "ms";break;
		case R.id.mssql: mConf.mLanguage = "mssql";break;
		case R.id.mxml: mConf.mLanguage = "mxml";break;
		case R.id.n3: mConf.mLanguage = "n3";break;
		case R.id.nasal: mConf.mLanguage = "nasal";break;
		case R.id.nbc: mConf.mLanguage = "nbc";break;
		case R.id.nemerle: mConf.mLanguage = "nemerle";break;
		case R.id.netrexx: mConf.mLanguage = "netrexx";break;
		case R.id.nice: mConf.mLanguage = "nice";break;
		case R.id.nsis: mConf.mLanguage = "nsis";break;
		case R.id.nxc: mConf.mLanguage = "nxc";break;
		case R.id.oberon: mConf.mLanguage = "oberon";break;
		case R.id.objc: mConf.mLanguage = "objc";break;
		case R.id.ocaml: mConf.mLanguage = "ocaml";break;
		case R.id.octave: mConf.mLanguage = "octave";break;
		case R.id.oorexx: mConf.mLanguage = "oorexx";break;
		case R.id.os: mConf.mLanguage = "os";break;
		case R.id.oz: mConf.mLanguage = "oz";break;
		case R.id.paradox: mConf.mLanguage = "paradox";break;
		case R.id.pas: mConf.mLanguage = "pas";break;
		case R.id.pdf: mConf.mLanguage = "pdf";break;
		case R.id.perl: mConf.mLanguage = "perl";break;
		case R.id.php: mConf.mLanguage = "php";break;
		case R.id.pike: mConf.mLanguage = "pike";break;
		case R.id.pl1: mConf.mLanguage = "pl1";break;
		case R.id.plperl: mConf.mLanguage = "plperl";break;
		case R.id.plpython: mConf.mLanguage = "plpython";break;
		case R.id.pltcl: mConf.mLanguage = "pltcl";break;
		case R.id.pov: mConf.mLanguage = "pov";break;
		case R.id.pro: mConf.mLanguage = "pro";break;
		case R.id.progress: mConf.mLanguage = "progress";break;
		case R.id.ps: mConf.mLanguage = "ps";break;
		case R.id.ps1: mConf.mLanguage = "ps1";break;
		case R.id.psl: mConf.mLanguage = "psl";break;
		case R.id.pure: mConf.mLanguage = "pure";break;
		case R.id.pyrex: mConf.mLanguage = "pyrex";break;
		case R.id.q: mConf.mLanguage = "q";break;
		case R.id.qmake: mConf.mLanguage = "qmake";break;
		case R.id.qml: mConf.mLanguage = "qml";break;
		case R.id.qu: mConf.mLanguage = "qu";break;
		case R.id.r: mConf.mLanguage = "r";break;
		case R.id.rebol: mConf.mLanguage = "rebol";break;
		case R.id.rexx: mConf.mLanguage = "rexx";break;
		case R.id.rnc: mConf.mLanguage = "rnc";break;
		case R.id.rpg: mConf.mLanguage = "rpg";break;
		case R.id.rpl: mConf.mLanguage = "rpl";break;
		case R.id.rs: mConf.mLanguage = "rs";break;
		case R.id.ruby: mConf.mLanguage = "ruby";break;
		case R.id.s: mConf.mLanguage = "s";break;
		case R.id.sas: mConf.mLanguage = "sas";break;
		case R.id.scala: mConf.mLanguage = "scala";break;
		case R.id.scilab: mConf.mLanguage = "scilab";break;
		case R.id.scss: mConf.mLanguage = "scss";break;
		case R.id.sh: mConf.mLanguage = "sh";break;
		case R.id.small: mConf.mLanguage = "small";break;
		case R.id.smalltalk: mConf.mLanguage = "smalltalk";break;
		case R.id.sml: mConf.mLanguage = "sml";break;
		case R.id.snmp: mConf.mLanguage = "snmp";break;
		case R.id.snobol: mConf.mLanguage = "snobol";break;
		case R.id.spec: mConf.mLanguage = "spec";break;
		case R.id.spn: mConf.mLanguage = "spn";break;
		case R.id.sql: mConf.mLanguage = "sql";break;
		case R.id.squirrel: mConf.mLanguage = "squirrel";break;
		case R.id.styl: mConf.mLanguage = "styl";break;
		case R.id.swift: mConf.mLanguage = "swift";break;
		case R.id.sybase: mConf.mLanguage = "sybase";break;
		case R.id.tcl: mConf.mLanguage = "tcl";break;
		case R.id.tcsh: mConf.mLanguage = "tcsh";break;
		case R.id.tex: mConf.mLanguage = "tex";break;
		case R.id.ts: mConf.mLanguage = "ts";break;
		case R.id.tsql: mConf.mLanguage = "tsql";break;
		case R.id.ttcn3: mConf.mLanguage = "ttcn3";break;
		case R.id.txt: mConf.mLanguage = "txt";break;
		case R.id.upc: mConf.mLanguage = "upc";break;
		case R.id.vala: mConf.mLanguage = "vala";break;
		case R.id.vb: mConf.mLanguage = "vb";break;
		case R.id.verilog: mConf.mLanguage = "verilog";break;
		case R.id.vhd: mConf.mLanguage = "vhd";break;
		case R.id.xml: mConf.mLanguage = "xml";break;
		case R.id.xpp: mConf.mLanguage = "xpp";break;
		case R.id.yaiff: mConf.mLanguage = "yaiff";break;
		case R.id.yang: mConf.mLanguage = "yang";break;
		case R.id.znn: mConf.mLanguage = "znn";break;
			
		case R.id.acid:mConf.mTheme = "acid";break;
		case R.id.aiseered:mConf.mTheme = "aiseered";break;
		case R.id.andes:mConf.mTheme = "andes";break;
		case R.id.anotherdark:mConf.mTheme = "anotherdark";break;
		case R.id.autumn:mConf.mTheme = "autumn";break;
		case R.id.baycomb:mConf.mTheme = "baycomb";break;
		case R.id.bclear:mConf.mTheme = "bclear";break;
		case R.id.biogoo:mConf.mTheme = "biogoo";break;
		case R.id.bipolar:mConf.mTheme = "bipolar";break;
		case R.id.blacknblue:mConf.mTheme = "blacknblue";break;
		case R.id.bluegreen:mConf.mTheme = "bluegreen";break;
		case R.id.breeze:mConf.mTheme = "breeze";break;
		case R.id.bright:mConf.mTheme = "bright";break;
		case R.id.camo:mConf.mTheme = "camo";break;
		case R.id.candy:mConf.mTheme = "candy";break;
		case R.id.clarity:mConf.mTheme = "clarity";break;
		case R.id.dante:mConf.mTheme = "dante";break;
		case R.id.darkblue:mConf.mTheme = "darkblue";break;
		case R.id.darkbone:mConf.mTheme = "darkbone";break;
		case R.id.darkness:mConf.mTheme = "darkness";break;
		case R.id.darkslategray:mConf.mTheme = "darkslategray";break;
		case R.id.darkspectrum:mConf.mTheme = "darkspectrum";break;
		case R.id.denim:mConf.mTheme = "denim";break;
		case R.id.dusk:mConf.mTheme = "dusk";break;
		case R.id.earendel:mConf.mTheme = "earendel";break;
		case R.id.easter:mConf.mTheme = "easter";break;
		case R.id.edit_anjuta:mConf.mTheme = "edit-anjuta";break;
		case R.id.edit_eclipse:mConf.mTheme = "edit-eclipse";break;
		case R.id.edit_emacs:mConf.mTheme = "edit-emacs";break;
		case R.id.edit_flashdevelop:mConf.mTheme = "edit-flashdevelop";break;
		case R.id.edit_gedit:mConf.mTheme = "edit-gedit";break;
		case R.id.edit_jedit:mConf.mTheme = "edit-jedit";break;
		case R.id.edit_kwrite:mConf.mTheme = "edit-kwrite";break;
		case R.id.edit_matlab:mConf.mTheme = "edit-matlab";break;
		case R.id.edit_msvs2008:mConf.mTheme = "edit-msvs2008";break;
		case R.id.edit_nedit:mConf.mTheme = "edit-nedit";break;
		case R.id.edit_vim_dark:mConf.mTheme = "edit-vim-dark";break;
		case R.id.edit_vim:mConf.mTheme = "edit-vim";break;
		case R.id.edit_xcode:mConf.mTheme = "edit-xcode";break;
		case R.id.ekvoli:mConf.mTheme = "ekvoli";break;
 		case R.id.freya:mConf.mTheme = "freya";break;
		case R.id.fruit:mConf.mTheme = "fruit";break;
		case R.id.golden:mConf.mTheme = "golden";break;
		case R.id.greenlcd:mConf.mTheme = "greenlcd";break;
		case R.id.kellys:mConf.mTheme = "kellys";break;
		case R.id.leo:mConf.mTheme = "leo";break;
		case R.id.lucretia:mConf.mTheme = "lucretia";break;
		case R.id.manxome:mConf.mTheme = "manxome";break;
		case R.id.maroloccio:mConf.mTheme = "maroloccio";break;
		case R.id.matrix:mConf.mTheme = "matrix";break;
		case R.id.moe:mConf.mTheme = "moe";break;
 		case R.id.moria:mConf.mTheme = "moria";break;
		case R.id.navajo_night:mConf.mTheme = "navajo-night";break;
		case R.id.navy:mConf.mTheme = "navy";break;
		case R.id.neon:mConf.mTheme = "neon";break;
		case R.id.night:mConf.mTheme = "night";break;
		case R.id.nightshimmer:mConf.mTheme = "nightshimmer";break;
		case R.id.nuvola:mConf.mTheme = "nuvola";break;
		case R.id.olive:mConf.mTheme = "olive";break;
		case R.id.orion:mConf.mTheme = "orion";break;
		case R.id.oxygenated:mConf.mTheme = "oxygenated";break;
		case R.id.pablo:mConf.mTheme = "pablo";break;
		case R.id.peaksea:mConf.mTheme = "peaksea";break;
		case R.id.print:mConf.mTheme = "print";break;
		case R.id.rand01:mConf.mTheme = "rand01";break;
		case R.id.rdark:mConf.mTheme = "rdark";break;
		case R.id.relaxedgreen:mConf.mTheme = "relaxedgreen";break;
		case R.id.rootwater:mConf.mTheme = "rootwater";break;
		case R.id.seashell:mConf.mTheme = "seashell";break;
		case R.id.solarized_dark:mConf.mTheme = "solarized-dark";break;
		case R.id.tabula:mConf.mTheme = "tabula";break;
		case R.id.tcsoft:mConf.mTheme = "tcsoft";break;
		case R.id.the:mConf.mTheme = "the";break;
 		case R.id.whitengrey:mConf.mTheme = "whitengrey";break;
		case R.id.xoria256:mConf.mTheme = "xoria256";break;
		case R.id.zellner:mConf.mTheme = "zellner";break;
		case R.id.zenburn:mConf.mTheme = "zenburn";break;
		case R.id.zmrok:mConf.mTheme = "zmrok";break;
			
		
		case R.id.action_about:
			Dialog dialog1 = new Dialog(this);  
	        dialog1.setContentView(R.layout.about);  
	        dialog1.setTitle("关于");  
	        dialog1.show();  
			break;
		default:
			break;
		}

		mHighlightEdit.loadFromConfigure(mConf);
		mHighlightEdit.setSource(mTextcode);

		return super.onOptionsItemSelected(item);
	}
}
