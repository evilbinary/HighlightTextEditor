HighlightTextEditor
An android HighlightTextEditor

/* Copyright (C) 2015 evilbinary. This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. If not, see http://www.gnu.org/licenses/. */

#语法高亮
HighlightTextEditor是一个安卓代码语法高亮控件，目前已经支持200多种语言，近90多种主题配色方案，同时支持lua扩展，以及自定义语言配置。强烈推荐，一款不可多得的开源控件。
###支持的语言：
	abap4.lang        bat.lang          crk.lang          graphviz.lang     	lilypond.lang     n3.lang           plperl.lang       rs.lang           tex.lang
	abc.lang          bbcode.lang       csharp.lang       haskell.lang      limbo.lang        	nasal.lang        plpython.lang     ruby.lang         ts.lang
	abnf.lang         bcpl.lang         css.lang          haxe.lang         lindenscript.lang nbc.lang          pltcl.lang        s.lang            tsql.lang
	actionscript.lang bibtex.lang       d.lang            hcl.lang          lisp.lang         nemerle.lang      pov.lang          sas.lang          ttcn3.lang
	ada.lang          biferno.lang      dart.lang         html.lang         logtalk.lang      netrexx.lang      pro.lang          scala.lang        txt.lang
	agda.lang         bison.lang        diff.lang         httpd.lang        lotos.lang        nice.lang         progress.lang     scilab.lang       upc.lang
	algol.lang        blitzbasic.lang   dylan.lang        icon.lang         lotus.lang        nsis.lang         ps.lang           scss.lang         vala.lang
	ampl.lang         bms.lang          ebnf.lang         idl.lang          lua.lang          nxc.lang          ps1.lang          sh.lang           vb.lang
	amtrix.lang       bnf.lang          eiffel.lang       idlang.lang       luban.lang        oberon.lang       psl.lang          small.lang        verilog.lang
	applescript.lang  boo.lang          erlang.lang       inc_luatex.lang   make.lang         objc.lang         pure.lang         smalltalk.lang    vhd.lang
	arc.lang          c.lang            euphoria.lang     informix.lang     maple.lang        ocaml.lang        pyrex.lang        sml.lang          xml.lang
	arm.lang          ceylon.lang       express.lang      ini.lang          matlab.lang       octave.lang       python.lang       snmp.lang         xpp.lang
	as400cl.lang      charmm.lang       fame.lang         innosetup.lang    maya.lang         oorexx.lang       q.lang            snobol.lang       yaiff.lang
	ascend.lang       chill.lang        felix.lang        interlis.lang     mercury.lang      os.lang           qmake.lang        spec.lang         yang.lang
	asp.lang          clean.lang        fortran77.lang    io.lang           miranda.lang      oz.lang           qml.lang          spn.lang          znn.lang
	aspect.lang       clearbasic.lang   fortran90.lang    jasmin.lang       mod2.lang         paradox.lang      qu.lang           sql.lang
	assembler.lang    clipper.lang      frink.lang        java.lang         mod3.lang         pas.lang          r.lang            squirrel.lang
	ats.lang          clojure.lang      fsharp.lang       js.lang           modelica.lang     pdf.lang          rebol.lang        styl.lang
	autohotkey.lang   clp.lang          fx.lang           jsp.lang          moon.lang         perl.lang         rexx.lang         swift.lang
	autoit.lang       cobol.lang        gambas.lang       ldif.lang         ms.lang           php.lang          rnc.lang          sybase.lang
	avenue.lang       coldfusion.lang   gdb.lang          less.lang         mssql.lang        pike.lang         rpg.lang          tcl.lang
	awk.lang          conf.lang         go.lang           lhs.lang          mxml.lang         pl1.lang          rpl.lang          tcsh.lang
###支持的配色主题：
	acid.theme              bright.theme            earendel.theme          edit-vim-	dark.theme     manxome.theme           olive.theme             solarized-light.theme
	aiseered.theme          camo.theme              easter.theme            edit-vim.theme          	maroloccio.theme        orion.theme             tabula.theme
	andes.theme             candy.theme             edit-anjuta.theme       edit-xcode.theme        	matrix.theme            oxygenated.theme        tcsoft.theme
	anotherdark.theme       clarity.theme           edit-eclipse.theme      ekvoli.theme            	moe.theme               pablo.theme             the.theme
	autumn.theme            dante.theme             edit-emacs.theme        fine_blue.theme         	molokai.theme           peaksea.theme           vampire.theme
	baycomb.theme           darkblue.theme          edit-flashdevelop.theme freya.theme             	moria.theme             print.theme             whitengrey.theme
	bclear.theme            darkbone.theme          edit-gedit.theme        fruit.theme             	navajo-night.theme      rand01.theme            xoria256.theme
	biogoo.theme            darkness.theme          edit-jedit.theme        golden.theme            	navy.theme              rdark.theme             zellner.theme
	bipolar.theme           darkslategray.theme     edit-kwrite.theme       greenlcd.theme          	neon.theme              relaxedgreen.theme      zenburn.theme
	blacknblue.theme        darkspectrum.theme      edit-matlab.theme       kellys.theme            	night.theme             rootwater.theme         zmrok.theme
	bluegreen.theme         denim.theme             edit-msvs2008.theme     leo.theme               	nightshimmer.theme      seashell.theme
	breeze.theme            dusk.theme              edit-nedit.theme        lucretia.theme          	nuvola.theme            solarized-dark.theme
#截图
* 代码编辑界面 
	<br><img src="https://github.com/evilbinary/HighlightTextEditor/raw/master/data/screenshot.jpg" alt="编辑界面" style="max-width:380px;" width="380px">

#历史记录
* a199ea2 - (HEAD, origin/master, master) 图片宽度修改 (evilbinary)
* 046d56e - 高亮基本版本完成 (evilbinary)
* 3bfce62 - 修复0，0位置文字获取不全问题 (evilbinary)
* d51e13a - 增加语法解析后端 (evilbinary)
* d6acd76 - 修改大小 (evilbinary)
* a1d48bb - modify pic (evilbinary)
* 65cbc6a - 修改图片大小 (evilbinary)
* f90e1d5 - 代码截图 (evilbinary)
* 43b466e - 增加显示行数 (evilbinary)
* 7a766af - 移动文件，重新定义 (evilbinary)
* 62c7078 - 增加css外部加载样式功能 (evilbinary)
* d48d344 - 去除标题显示 (evilbinary)
* 3a6a782 - 增加解析支持 (evilbinary)
* b198bdd - modify readme (evilbinary)
* ef7d5c6 - Create README.md (evilbinary)
* 95f3a12 - first version (evilbinary)


#Todo
* 代码渲染基本功能。 ［完成］
* 支持200多种语言。 ［完成］
* 支持82种配色主题。 ［完成］
* 支持lua扩展语法高亮检测。	 ［基本完成］
* 若干bug修复。
* 主题语言更换功能。
* 字体大小样式功能。
* 表情功能。（待定）

